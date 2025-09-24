package com.ruk.payments.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ruk.payments.dto.SaleDetails;
import com.ruk.payments.dto.SaleItems;

import java.math.BigDecimal;
import java.util.Arrays;

/**
 * Test class to verify JSON serialization format matches EPP expectations
 */
public class JsonSerializationTest {
    
    public static void main(String[] args) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            
            // Create test data matching your example
            SaleDetails saleDetails = new SaleDetails();
            saleDetails.setSaleDetailId(0);
            saleDetails.setApplicationCode("4f0bbbc1-8797-47db-bfa1-b470e0e3ae51");
            saleDetails.setOrderKey("1362666474");
            saleDetails.setFirstName("John");
            saleDetails.setLastName("Smith");
            saleDetails.setAddress1("400 Market Street");
            saleDetails.setAddress2(null);
            saleDetails.setCity("Harrisburg");
            saleDetails.setStateCode("PA");
            saleDetails.setZipCode("17111");
            saleDetails.setTotalAmount(new BigDecimal("100.0"));
            saleDetails.setApplicationUniqueId(null);
            saleDetails.setPaymentAccountType("CC");
            saleDetails.setEmail("testuser@gmail.com");
            
            // Create items
            SaleItems item1 = new SaleItems();
            item1.setSaleItemId(0);
            item1.setCount(2);
            item1.setDescription("Product One");
            item1.setAmount(new BigDecimal("25.0"));
            item1.setItemKey("D9-5B-48-95");
            
            SaleItems item2 = new SaleItems();
            item2.setSaleItemId(0);
            item2.setCount(5);
            item2.setDescription("Product Two");
            item2.setAmount(new BigDecimal("10.0"));
            item2.setItemKey("D9-5B-48-95");
            
            saleDetails.setItems(Arrays.asList(item1, item2));
            
            // Serialize to JSON
            String json = objectMapper.writeValueAsString(saleDetails);
            
            System.out.println("Generated JSON:");
            System.out.println(json);
            
            System.out.println("\nExpected JSON:");
            System.out.println("{\"SaleDetailId\":0,\"ApplicationCode\":\"4f0bbbc1-8797-47db-bfa1-b470e0e3ae51\",\"OrderKey\":\"1362666474\",\"FirstName\":\"John\",\"LastName\":\"Smith\",\"Address1\":\"400 Market Street\",\"Address2\":null,\"City\":\"Harrisburg\",\"StateCode\":\"PA\",\"ZipCode\":\"17111\",\"TotalAmount\":100.0,\"Items\":[{\"SaleItemId\":0,\"Count\":2,\"Description\":\"Product One\",\"Amount\":25.0,\"ItemKey\":\"D9-5B-48-95\"},{\"SaleItemId\":0,\"Count\":5,\"Description\":\"Product Two\",\"Amount\":10.0,\"ItemKey\":\"D9-5B-48-95\"}],\"ApplicationUniqueId\":null,\"PaymentAccountType\":\"CC\",\"EmailId\":\"testuser@gmail.com\"}");
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}