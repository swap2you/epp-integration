package com.ruk.payments.service;package com.ruk.payments.service;



import com.fasterxml.jackson.databind.ObjectMapper;import com.fasterxml.jackson.core.JsonProcessingException;

import com.ruk.payments.config.EppProperties;import com.fasterxml.jackson.databind.ObjectMapper;

import com.ruk.payments.dto.SaleDetails;import com.ruk.payments.config.EppProperties;

import com.ruk.payments.entity.EppTransaction;import com.ruk.payments.dto.SaleDetails;

import com.ruk.payments.repo.EppTransactionRepository;import com.ruk.payments.entity.EppTransaction;

import org.springframework.stereotype.Service;import com.ruk.payments.repo.EppTransactionRepository;

import org.springframework.transaction.annotation.Transactional;import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

/**

 * EPP Integration Service - Clean and Simple/**

 */ * EPP Integration Service - Simplified

@Service */

public class EppClient {@Service

    private final EppProperties eppProperties;public class EppClient {

    private final EppTransactionRepository repo;    private final EppProperties eppProperties;

    private final ObjectMapper objectMapper;    private final EppTransactionRepository repo;

    private final ObjectMapper objectMapper;

    public EppClient(EppProperties eppProperties, EppTransactionRepository repo, ObjectMapper objectMapper) {

        this.eppProperties = eppProperties;    public EppClient(EppProperties eppProperties, EppTransactionRepository repo, ObjectMapper objectMapper) {

        this.repo = repo;        this.eppProperties = eppProperties;

        this.objectMapper = objectMapper;        this.repo = repo;

    }        this.objectMapper = objectMapper;

    }

    public String buildHostedCheckoutForm(SaleDetails saleDetails) {

        try {    public String buildHostedCheckoutForm(SaleDetails saleDetails) {

            EppTransaction transaction = persistTransaction(saleDetails);        String json;

            String jsonPayload = objectMapper.writeValueAsString(saleDetails);        try {

                        json = objectMapper.writeValueAsString(saleDetails);

            StringBuilder sb = new StringBuilder();        } catch (JsonProcessingException e) {

            sb.append("<form id='__PostForm' name='__PostForm' action='")            throw new RuntimeException("Failed to serialize SaleDetails", e);

              .append(eppProperties.getPaymentGatewayIndexUrl())        }

              .append("' method='POST'>")        // Pluggable encryption stub (TBD)

              .append("<input type='hidden' name='saleDetail' value='")        String encryptedPayload = json; // TODO: replace with encryption logic

              .append(jsonPayload.replace("\"", "&quot;"))

              .append("'/>")        // Use Rahul's proven JavaScript form submission approach

              .append("</form>")        StringBuilder sb = new StringBuilder();

              .append("<script>document.__PostForm.submit();</script>");        sb

                    //   .append("<html><body>")

            return sb.toString();          .append("<form id='__PostForm' name='__PostForm' action='").append(eppProperties.getPaymentGatewayIndexUrl()).append("' method='POST'>")

        } catch (Exception e) {          .append("<input type='hidden' name='saleDetail' value='").append((encryptedPayload)).append("'/>")

            throw new RuntimeException("Failed to build EPP form", e);          .append("</form>")

        }          .append("<script language='javascript'>var v__PostForm=document.__PostForm;v__PostForm.submit();</script>")

    }        //   .append("</body></html>")

        ;

    @Transactional        return sb.toString();

    private EppTransaction persistTransaction(SaleDetails saleDetails) {    }

        return repo.findByOrderKey(saleDetails.getOrderKey())

                .orElseGet(() -> {}

                    EppTransaction transaction = new EppTransaction();
                    transaction.setApplicationCode(saleDetails.getApplicationCode());
                    transaction.setOrderKey(saleDetails.getOrderKey());
                    transaction.setAmount(saleDetails.getTotalAmount());
                    transaction.setCustomerName(saleDetails.getFirstName() + " " + saleDetails.getLastName());
                    transaction.setCustomerEmail(saleDetails.getEmail());
                    transaction.setStatus("PENDING");
                    return repo.save(transaction);
                });
    }
}