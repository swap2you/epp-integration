package com.ruc.payments;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ruc.payments.config.EppProperties;
import com.ruc.payments.dto.SaleDetails;
import com.ruc.payments.dto.SaleItems;
import com.ruc.payments.entity.EppTransaction;
import com.ruc.payments.repo.EppTransactionRepository;
import com.ruc.payments.service.EppClient;

public class EppClientTest {
    private EppClient eppClient;
    private EppTransactionRepository repo;
    private EppProperties props;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        repo = Mockito.mock(EppTransactionRepository.class);
        props = new EppProperties();
        props.setPaymentGatewayIndexUrl("https://epp.example.com/Payment/Index");
        objectMapper = new ObjectMapper();
        eppClient = new EppClient(props, repo, objectMapper);
    }

    @Test
    void buildHostedCheckoutForm_containsForm() {
        SaleItems item = new SaleItems();
        item.setSaleItemId(1);
        item.setCount(1);
        item.setDescription("Test Item");
        item.setAmount(new BigDecimal("10.00"));
        item.setItemKey("test-item-key-sha512");

        SaleDetails details = new SaleDetails();
        details.setOrderKey("ORD123");
        details.setApplicationUniqueId("APP456");
        details.setApplicationCode("3256d54a-9e63-4c7d-b2f9-a2897ec82aab");
        details.setTotalAmount(new BigDecimal("10.00"));
        details.setFirstName("John");
        details.setLastName("Doe");
        details.setAddress1("123 Main St");
        details.setCity("Wellington");
        details.setStateCode("WE");
        details.setZipCode("6011");
        details.setEmail("test@example.com");
        details.setItems(Collections.singletonList(item));

        String html = eppClient.buildHostedCheckoutForm(details);
        assertTrue(html.contains("<form"));
        assertTrue(html.contains("<form"));
        assertTrue(html.contains("Payment/Index"));
    }

    @Test
    void upsertTransaction_insertsOrUpdates() {
        when(repo.findByOrderKeyAndApplicationUniqueId(anyString(), anyString()))
                .thenReturn(Optional.empty());
        when(repo.save(any(EppTransaction.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        EppTransaction tx = eppClient.upsertTransaction("ORD1", "APP1", "APP",
                new BigDecimal("1.00"), "a@b.com", "{}", "{}", null, null);
        assertEquals("ORD1", tx.getOrderKey());
        assertEquals("APP1", tx.getApplicationUniqueId());
        assertEquals("APP", tx.getStatus());
    }
}
