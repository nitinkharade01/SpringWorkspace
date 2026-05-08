package com.nitin.payment.reconciliation.service;

import com.nitin.payment.common.CommonConstants;
import com.nitin.payment.common.event.ReconciliationCompletedEvent;
import com.nitin.payment.common.exception.FileProcessingException;
import com.nitin.payment.common.exception.ResourceNotFoundException;
import com.nitin.payment.reconciliation.entity.*;
import com.nitin.payment.reconciliation.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReconciliationService {
    private final SettlementFileRepository fileRepository;
    private final ReconciliationRecordRepository recordRepository;
    private final ReconciliationSummaryRepository summaryRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public ReconciliationSummary upload(MultipartFile file) {
        try {
            SettlementFile settlementFile = new SettlementFile();
            settlementFile.setOriginalFileName(file.getOriginalFilename());
            settlementFile.setFileType(Objects.requireNonNullElse(file.getContentType(), "unknown"));
            settlementFile.setStatus("PROCESSING");
            SettlementFile savedFile = fileRepository.save(settlementFile);
            List<BankRow> rows = parse(file);
            Map<String, Long> seen = new HashMap<>();
            List<ReconciliationRecord> records = rows.stream().map(row -> toRecord(savedFile.getId(), row, seen)).toList();
            recordRepository.saveAll(records);
            ReconciliationSummary summary = buildSummary(savedFile.getId(), records);
            savedFile.setStatus("COMPLETED");
            fileRepository.save(savedFile);
            kafkaTemplate.send(CommonConstants.RECONCILIATION_COMPLETED_TOPIC, savedFile.getId().toString(),
                    new ReconciliationCompletedEvent(savedFile.getId(), summary.getMatchedCount(), summary.getMismatchedCount(), summary.getMissingCount(), Instant.now()));
            log.info("Reconciliation completed fileId={}", savedFile.getId());
            return summary;
        } catch (Exception ex) {
            throw new FileProcessingException("Unable to process settlement file", ex);
        }
    }

    public ReconciliationSummary summary(Long fileId) {
        return summaryRepository.findByFileId(fileId).orElseThrow(() -> new ResourceNotFoundException("Summary not found for file: " + fileId));
    }

    public List<ReconciliationRecord> mismatches(Long fileId) {
        return recordRepository.findByFileIdAndReconciliationStatusNot(fileId, "MATCHED");
    }

    public String report(Long fileId) {
        StringBuilder csv = new StringBuilder("internalTransactionId,bankTransactionId,internalAmount,bankAmount,internalStatus,bankStatus,status,reason\n");
        recordRepository.findByFileId(fileId).forEach(r -> csv.append(String.join(",",
                safe(r.getInternalTransactionId()), safe(r.getBankTransactionId()), value(r.getInternalAmount()), value(r.getBankAmount()),
                safe(r.getInternalStatus()), safe(r.getBankStatus()), safe(r.getReconciliationStatus()), safe(r.getMismatchReason()))).append('\n'));
        return csv.toString();
    }

    private List<BankRow> parse(MultipartFile file) throws Exception {
        String name = Objects.requireNonNullElse(file.getOriginalFilename(), "").toLowerCase();
        if (name.endsWith(".xlsx")) {
            List<BankRow> rows = new ArrayList<>();
            Workbook workbook = WorkbookFactory.create(file.getInputStream());
            Sheet sheet = workbook.getSheetAt(0);
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row != null) rows.add(new BankRow(cell(row, 0), cell(row, 1), new BigDecimal(cell(row, 2)), cell(row, 3)));
            }
            return rows;
        }
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            return reader.lines().skip(1)
                    .filter(line -> !line.isBlank())
                    .map(line -> line.split(","))
                    .map(cols -> new BankRow(cols[0], cols[1], new BigDecimal(cols[2]), cols[3]))
                    .toList();
        }
    }

    private ReconciliationRecord toRecord(Long fileId, BankRow row, Map<String, Long> seen) {
        ReconciliationRecord record = new ReconciliationRecord();
        record.setFileId(fileId);
        record.setInternalTransactionId(row.internalTransactionId());
        record.setBankTransactionId(row.bankTransactionId());
        record.setInternalAmount(row.amount());
        record.setBankAmount(row.amount());
        record.setInternalStatus("SUCCESS");
        record.setBankStatus(row.status());
        seen.merge(row.bankTransactionId(), 1L, Long::sum);
        if (seen.get(row.bankTransactionId()) > 1) {
            record.setReconciliationStatus("DUPLICATE");
            record.setMismatchReason("Duplicate bank transaction id");
        } else if (!"SUCCESS".equalsIgnoreCase(row.status())) {
            record.setReconciliationStatus("MISMATCHED");
            record.setMismatchReason("Bank status differs from expected success");
        } else {
            record.setReconciliationStatus("MATCHED");
        }
        return record;
    }

    private ReconciliationSummary buildSummary(Long fileId, List<ReconciliationRecord> records) {
        ReconciliationSummary summary = new ReconciliationSummary();
        summary.setFileId(fileId);
        summary.setTotalRecords(records.size());
        summary.setMatchedCount(records.stream().filter(r -> "MATCHED".equals(r.getReconciliationStatus())).count());
        summary.setMismatchedCount(records.stream().filter(r -> "MISMATCHED".equals(r.getReconciliationStatus())).count());
        summary.setDuplicateCount(records.stream().filter(r -> "DUPLICATE".equals(r.getReconciliationStatus())).count());
        summary.setMissingCount(records.stream().filter(r -> "MISSING".equals(r.getReconciliationStatus())).count());
        return summaryRepository.save(summary);
    }

    private String cell(Row row, int index) {
        Cell cell = row.getCell(index);
        return cell == null ? "" : cell.toString();
    }

    private String safe(String value) {
        return value == null ? "" : value;
    }

    private String value(BigDecimal value) {
        return value == null ? "" : value.toPlainString();
    }

    private record BankRow(String internalTransactionId, String bankTransactionId, BigDecimal amount, String status) {
    }
}
