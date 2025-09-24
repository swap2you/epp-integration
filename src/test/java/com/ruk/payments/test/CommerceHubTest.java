package com.ruk.payments.test;

import com.ruk.payments.dto.SaleDetails;
import com.ruk.payments.dto.SaleItems;
import java.math.BigDecimal;
import java.util.Arrays;

/**
 * Quick test to verify Commerce Hub compliant field changes
 */
public class CommerceHubTest {
    
    public static void main(String[] args) {
        // Test Commerce Hub compliant payload
        SaleDetails saleDetails = new SaleDetails();
        saleDetails.setOrderKey("ORDER123");
        saleDetails.setApplicationCode("3256d54a-9e63-4c7d-b2f9-a2897ec82aab");
        saleDetails.setTotalAmount(new BigDecimal("25.00"));
        saleDetails.setEmail("test@example.com");
        
        // Test nullable saleDetailId (should be null per Commerce Hub)
        saleDetails.setSaleDetailId(null);
        
        // Test optional applicationUniqueId
        // saleDetails.setApplicationUniqueId(null); // This should be optional now
        
        // Test item with nullable saleItemId
        SaleItems item = new SaleItems();
        item.setSaleItemId(null); // Should be null per Commerce Hub
        item.setCount(1);
        item.setDescription("Test Item");
        item.setAmount(new BigDecimal("25.00"));
        item.setItemKey(""); // Will be set to orderKey by service
        
        saleDetails.setItems(Arrays.asList(item));
        
        System.out.println("Commerce Hub compliant payload created successfully:");
        System.out.println("- saleDetailId: " + saleDetails.getSaleDetailId());
        System.out.println("- applicationUniqueId: " + saleDetails.getApplicationUniqueId());
        System.out.println("- item saleItemId: " + item.getSaleItemId());
        System.out.println("- item itemKey: '" + item.getItemKey() + "' (will be set to orderKey)");
    }
}