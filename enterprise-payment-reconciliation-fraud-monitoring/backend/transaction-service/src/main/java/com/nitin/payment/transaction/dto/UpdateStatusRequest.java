package com.nitin.payment.transaction.dto;

import com.nitin.payment.transaction.entity.TransactionStatus;
import jakarta.validation.constraints.NotNull;

public record UpdateStatusRequest(@NotNull TransactionStatus status, String remarks) {
}
