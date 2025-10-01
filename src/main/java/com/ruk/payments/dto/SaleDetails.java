package com.ruk.payments.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.util.List;

public class SaleDetails {
    
    // EPP expects 0 instead of null for numeric fields
    @JsonProperty("SaleDetailId")
    private Integer saleDetailId = 0;
    
    @NotBlank
    @Size(max = 50) // Updated to accommodate UUID format application codes
    @JsonProperty("ApplicationCode")
    private String applicationCode;
    
    @NotBlank
    @Size(max = 200) // EPP max length: 200
    @JsonProperty("OrderKey")
    private String orderKey; // List of Invoice IDs as a comma-delimited string
    
    @NotBlank
    @Size(max = 20) // EPP max length: 20
    @JsonProperty("FirstName")
    private String firstName;
    
    @NotBlank
    @Size(max = 20) // EPP max length: 20
    @JsonProperty("LastName")
    private String lastName;
    
    @NotBlank
    @Size(max = 100) // EPP max length: 100
    @JsonProperty("Address1")
    private String address1;
    
    @Size(max = 100) // EPP max length: 100
    @JsonProperty("Address2")
    private String address2;
    
    @NotBlank
    @Size(max = 100) // EPP max length: 100
    @JsonProperty("City")
    private String city;
    
    @NotBlank
    @Size(max = 2) // EPP max length: 2
    @JsonProperty("StateCode")
    private String stateCode;
    
    @NotBlank
    @Size(max = 10) // EPP max length: 10
    @JsonProperty("ZipCode")
    private String zipCode;
    
    @NotNull
    @DecimalMin("0.00")
    @JsonProperty("TotalAmount")
    private BigDecimal totalAmount; // Sum of all the payment items = Total payment amount
    
    @NotEmpty
    @Valid
    @JsonProperty("Items")
    private List<SaleItems> items; // List of SaleItems object
    
    // @NotBlank
    @JsonProperty("ApplicationUniqueId")
    private String applicationUniqueId;
    
    @JsonProperty("PaymentAccountType")
    private String paymentAccountType; // Payment method used (Credit Card or ACH)
    
    @Email
    @NotBlank
    @Size(max = 100) // EPP max length: 100
    @JsonProperty("Email")
    private String email; // Email address of constituent submitting payment

    // Getters and Setters
    public Integer getSaleDetailId() { return saleDetailId; }
    public void setSaleDetailId(Integer saleDetailId) { this.saleDetailId = saleDetailId; }
    
    public String getApplicationCode() { return applicationCode; }
    public void setApplicationCode(String applicationCode) { this.applicationCode = applicationCode; }
    
    public String getOrderKey() { return orderKey; }
    public void setOrderKey(String orderKey) { this.orderKey = orderKey; }
    
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    
    public String getAddress1() { return address1; }
    public void setAddress1(String address1) { this.address1 = address1; }
    
    public String getAddress2() { return address2; }
    public void setAddress2(String address2) { this.address2 = address2; }
    
    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }
    
    public String getStateCode() { return stateCode; }
    public void setStateCode(String stateCode) { this.stateCode = stateCode; }
    
    public String getZipCode() { return zipCode; }
    public void setZipCode(String zipCode) { this.zipCode = zipCode; }
    
    public BigDecimal getTotalAmount() { return totalAmount; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }
    
    public List<SaleItems> getItems() { return items; }
    public void setItems(List<SaleItems> items) { this.items = items; }
    
    public String getApplicationUniqueId() { return applicationUniqueId; }
    public void setApplicationUniqueId(String applicationUniqueId) { this.applicationUniqueId = applicationUniqueId; }
    
    public String getPaymentAccountType() { return paymentAccountType; }
    public void setPaymentAccountType(String paymentAccountType) { this.paymentAccountType = paymentAccountType; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}
