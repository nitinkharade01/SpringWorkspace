package com.example.ingestionservice.dto;

import com.example.common.dto.DataEvent;

public class DataIngestionRequest {
    private String tenantId;
    private String source;
    private String dataType;
    private String payload;
    
    // ✅ ADD THIS GETTER (line ~12)
    public String getTenantId() { 
        return tenantId; 
    }
    
    public String getSource() { return source; }
    public void setSource(String source) { this.source = source; }
    
    public String getDataType() { return dataType; }
    public void setDataType(String dataType) { this.dataType = dataType; }
    
    public String getPayload() { return payload; }
    public void setPayload(String payload) { this.payload = payload; }
    
    // ✅ ADD SETTER too
    public void setTenantId(String tenantId) { 
        this.tenantId = tenantId; 
    }
}
