
package com.ruk.payments.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ruk.payments.config.EppProperties;
import com.ruk.payments.dto.SaleDetails;
import com.ruk.payments.entity.EppTransaction;
import com.ruk.payments.repo.EppTransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;


/**
 * EPP (Electronic Payment Platform) Integration Service.
 * 
 * This service handles all direct interactions with the Pennsylvania EPP system:
 * - Generates HTML forms for hosted checkout redirect
 * - Manages transaction persistence and idempotency 
 * - Formats data according to EPP specification requirements
 * - Handles JSON serialization with proper escaping for EPP forms
 * 
 * @see <a href="https://epp.beta.pa.gov">Pennsylvania EPP Documentation</a>
 */
@Service
public class EppClient {
    private final EppProperties eppProperties;
    private final EppTransactionRepository repo;
    private final ObjectMapper objectMapper;

    /**
     * Constructor for dependency injection.
     */
    public EppClient(EppProperties eppProperties, EppTransactionRepository repo, ObjectMapper objectMapper) {
        this.eppProperties = eppProperties;
        this.repo = repo;
        this.objectMapper = objectMapper;
    }

    /**
     * Builds an auto-submitting HTML form for EPP hosted checkout.
     *
     * @param saleDetails Sale details payload
     * @return HTML form as String
     */
    public String buildHostedCheckoutForm(SaleDetails saleDetails) {
        String json;
        try {
            json = objectMapper.writeValueAsString(saleDetails);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize SaleDetails", e);
        }
        // Pluggable encryption stub (TBD)
        String encryptedPayload = json; // TODO: replace with encryption logic

        StringBuilder sb = new StringBuilder();
        sb.append("<html><body onload='document.forms[0].submit()'>")
          .append("<form method='POST' action='").append(eppProperties.getPaymentGatewayIndexUrl()).append("'>")
          .append("<input type='hidden' name='payload' value='").append(escapeHtml(encryptedPayload)).append("'/>")
          .append("</form></body></html>");
        return sb.toString();
    }

    /**
     * Escapes HTML special characters for safe embedding in HTML.
     * For EPP integration, formats JSON exactly as required by Commerce Hub.
     * Returns JSON wrapped in quotes with internal quotes escaped.
     */
    private String escapeHtml(String s) {
        // For EPP integration, wrap the entire JSON in quotes and escape internal quotes
        // This matches Rahul's serialized format exactly
        String escapedJson = s.replace("\"", "\\\"");
        return "\"" + escapedJson + "\"";
    }

    /**
     * Idempotent upsert for EPP transaction.
     *
     * @param orderKey Order key
     * @param applicationUniqueId Application unique ID
     * @param status Transaction status
     * @param amount Transaction amount
     * @param email Payer email
     * @param rawRequest Raw request JSON
     * @param rawResponse Raw response JSON
     * @param authCode Authorization code
     * @param referenceNo Reference number
     * @return Upserted EppTransaction
     */
    @Transactional
    public EppTransaction upsertTransaction(String orderKey, String applicationUniqueId, String status,
                                            BigDecimal amount, String email, String rawRequest, String rawResponse,
                                            String authCode, String referenceNo) {
        Optional<EppTransaction> existing = repo.findByOrderKeyAndApplicationUniqueId(orderKey, applicationUniqueId);
        EppTransaction tx = existing.orElseGet(EppTransaction::new);
        tx.setOrderKey(orderKey);
        tx.setApplicationUniqueId(applicationUniqueId);
        tx.setStatus(status);
        tx.setAmount(amount);
        tx.setEmail(email);
        tx.setRawRequest(rawRequest);
        tx.setRawResponse(rawResponse);
        tx.setAuthCode(authCode);
        tx.setReferenceNo(referenceNo);
        return repo.save(tx);
    }
}
