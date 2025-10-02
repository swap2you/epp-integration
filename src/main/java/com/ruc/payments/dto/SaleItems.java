package com.ruc.payments.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

public class SaleItems {
    // EPP expects 0 instead of null for numeric fields
    @JsonProperty("SaleItemId")
    private Integer saleItemId = 0;
    
    @NotNull
    @Max(99999) // EPP max length: 5 digits
    @JsonProperty("Count")
    private Integer count;

    @NotBlank
    @Size(max = 200) // EPP max length: 200
    @JsonProperty("Description")
    private String description;

    @NotNull
    @DecimalMin("0.00")
    @JsonProperty("Amount")
    private BigDecimal amount;

    @Size(max = 500) // EPP max length: 500
    @JsonProperty("ItemKey")
    private String itemKey; // Should equal OrderKey value per Commerce Hub requirements

    // Getters and Setters
    public Integer getSaleItemId() { return saleItemId; }
    public void setSaleItemId(Integer saleItemId) { this.saleItemId = saleItemId; }
    
    public Integer getCount() { return count; }
    public void setCount(Integer count) { this.count = count; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    
    public String getItemKey() { return itemKey; }
    public void setItemKey(String itemKey) { this.itemKey = itemKey; }
    
    @Override
    public String toString() {
        return String.format("SaleItems{saleItemId=%d, count=%d, description='%s', amount=%s, itemKey='%s'}", 
                saleItemId, count, description, amount, itemKey);
    }
}
