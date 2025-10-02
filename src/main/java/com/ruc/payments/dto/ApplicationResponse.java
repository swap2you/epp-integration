package com.ruc.payments.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ApplicationResponse {
    @NotBlank
    @Size(max = 200) // EPP max length: 200
    private String orderKey; // List of Invoice IDs as a comma-delimited string

    private String applicationUniqueId; // EPG Sale Detail ID

    @NotBlank
    @Size(max = 256) // EPP max length: 256
    private String status; // Final status of the transaction

    private String errorMessage; // Error message from payment processor and EPG
    
    private String headerMessage; // Custom message to be displayed in the EPP payment receipt (optional)
    
    private String htmlMarkup; // Additional information in HTML format for EPP payment receipt (optional)

    // Legacy field for backward compatibility
    private String message;

    // Getters and Setters
    public String getOrderKey() { return orderKey; }
    public void setOrderKey(String orderKey) { this.orderKey = orderKey; }
    
    public String getApplicationUniqueId() { return applicationUniqueId; }
    public void setApplicationUniqueId(String applicationUniqueId) { this.applicationUniqueId = applicationUniqueId; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public String getErrorMessage() { return errorMessage; }
    public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }
    
    public String getHeaderMessage() { return headerMessage; }
    public void setHeaderMessage(String headerMessage) { this.headerMessage = headerMessage; }
    
    public String getHtmlMarkup() { return htmlMarkup; }
    public void setHtmlMarkup(String htmlMarkup) { this.htmlMarkup = htmlMarkup; }
    
    // Legacy getter/setter for backward compatibility
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}
