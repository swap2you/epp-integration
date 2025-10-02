package com.ruc.payments.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

public class EppResponse {
    @NotBlank
    @Size(max = 200) // EPP max length: 200
    private String orderKey;

    private String applicationUniqueId;
    
    @NotBlank
    @Size(max = 50) // Updated to accommodate UUID format application codes
    private String applicationCode;

    @NotBlank
    @Size(max = 256) // EPP max length: 256
    private String status; // Final status of the transaction

    private String errorMessage;
    
    private String cardHolderName; // First and last name of credit card holder
    
    @Size(max = 256) // EPP max length: 256
    private String address; // Street address
    
    @Size(max = 256) // EPP max length: 256
    private String city; // City or locality
    
    @Size(max = 256) // EPP max length: 256
    private String stateCode; // State or province name
    
    @Size(max = 10) // EPP max length: 10
    private String zipCode; // Postal code
    
    private BigDecimal totalAmount; // Total amount of the transaction
    
    private String emailId; // Email ID of Cardholder
    
    private String referenceNumber; // Authorization number from payment processor
    
    private String paymentAccountType; // Payment method used (Visa, Telecheck, etc.)

    // Legacy fields for backward compatibility
    private String authCode;
    private String referenceNo;

    // Getters and Setters
    public String getOrderKey() { return orderKey; }
    public void setOrderKey(String orderKey) { this.orderKey = orderKey; }
    
    public String getApplicationUniqueId() { return applicationUniqueId; }
    public void setApplicationUniqueId(String applicationUniqueId) { this.applicationUniqueId = applicationUniqueId; }
    
    public String getApplicationCode() { return applicationCode; }
    public void setApplicationCode(String applicationCode) { this.applicationCode = applicationCode; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public String getErrorMessage() { return errorMessage; }
    public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }
    
    public String getCardHolderName() { return cardHolderName; }
    public void setCardHolderName(String cardHolderName) { this.cardHolderName = cardHolderName; }
    
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    
    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }
    
    public String getStateCode() { return stateCode; }
    public void setStateCode(String stateCode) { this.stateCode = stateCode; }
    
    public String getZipCode() { return zipCode; }
    public void setZipCode(String zipCode) { this.zipCode = zipCode; }
    
    public BigDecimal getTotalAmount() { return totalAmount; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }
    
    public String getEmailId() { return emailId; }
    public void setEmailId(String emailId) { this.emailId = emailId; }
    
    public String getReferenceNumber() { return referenceNumber; }
    public void setReferenceNumber(String referenceNumber) { this.referenceNumber = referenceNumber; }
    
    public String getPaymentAccountType() { return paymentAccountType; }
    public void setPaymentAccountType(String paymentAccountType) { this.paymentAccountType = paymentAccountType; }
    
    // Legacy getters/setters for backward compatibility
    public String getAuthCode() { return authCode; }
    public void setAuthCode(String authCode) { this.authCode = authCode; }
    
    public String getReferenceNo() { return referenceNo; }
    public void setReferenceNo(String referenceNo) { this.referenceNo = referenceNo; }
}
