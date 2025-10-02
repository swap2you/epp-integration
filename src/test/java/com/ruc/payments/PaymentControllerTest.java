package com.ruc.payments;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ruc.payments.dto.SaleDetails;
import com.ruc.payments.dto.SaleItems;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import com.ruc.payments.service.EppClient;
import com.ruc.payments.service.PaymentService;
import com.ruc.payments.repo.EppTransactionRepository;
import com.ruc.payments.config.EppProperties;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest
@ActiveProfiles("test")
public class PaymentControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PaymentService paymentService;
    
    @MockBean
    private EppClient eppClient;

    @MockBean
    private EppTransactionRepository repo;

    @MockBean
    private EppProperties eppProperties;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void start_returnsHtmlForm() throws Exception {
        // Mock PaymentService to return HTML form
        String expectedHtml = "<form id='__PostForm' name='__PostForm' action='http://test.epp.url' method='POST'>" +
                "<input type='hidden' name='saleDetail' value='test-json'/>" +
                "</form>" +
                "<script language='javascript'>var v__PostForm=document.__PostForm;v__PostForm.submit();</script>";
        when(paymentService.initiatePayment(any(SaleDetails.class))).thenReturn(expectedHtml);
        
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
        details.setItems(java.util.Collections.singletonList(item));

        mockMvc.perform(post("/payments/epp/start")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(details)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML));
    }
}
