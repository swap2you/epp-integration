package com.ruc.payments.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ruc.payments.config.EppProperties;
import com.ruc.payments.dto.ApplicationResponse;
import com.ruc.payments.dto.EppResponse;
import com.ruc.payments.dto.SaleDetails;
import com.ruc.payments.entity.EppTransaction;
import com.ruc.payments.exception.PaymentProcessingException;
import com.ruc.payments.service.EppClient;
import com.ruc.payments.service.PaymentService;
import com.ruc.payments.service.TransactionService;
import com.ruc.payments.util.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PaymentServiceImpl implements PaymentService {
    
    private static final Logger logger = LoggerFactory.getLogger(PaymentServiceImpl.class);
    
    private final EppClient eppClient;
    private final TransactionService transactionService;
    private final ObjectMapper objectMapper;
    private final ModelMapper modelMapper;
    private final EppProperties eppProperties;
    
    public PaymentServiceImpl(
            EppClient eppClient,
            TransactionService transactionService,
            ObjectMapper objectMapper,
            ModelMapper modelMapper,
            EppProperties eppProperties) {
        this.eppClient = eppClient;
        this.transactionService = transactionService;
        this.objectMapper = objectMapper;
        this.modelMapper = modelMapper;
        this.eppProperties = eppProperties;
    }
    
    @Override
    public String initiatePayment(SaleDetails saleDetails) {
        logger.info("Initiating payment: orderKey={}", saleDetails.getOrderKey());
        
        validateEppEnabled();
        validatePaymentRequest(saleDetails);
        prepareSaleDetails(saleDetails);
        
        try {
            String rawRequest = objectMapper.writeValueAsString(saleDetails);
            handleInitialTransaction(saleDetails, rawRequest);
            return eppClient.buildHostedCheckoutForm(saleDetails);
        } catch (JsonProcessingException e) {
            throw new PaymentProcessingException("SERIALIZATION_ERROR", 
                "Failed to serialize payment request", e);
        } catch (PaymentProcessingException e) {
            throw e;
        } catch (Exception e) {
            throw new PaymentProcessingException("PAYMENT_INITIATION_FAILED", 
                "Payment initiation failed", e);
        }
    }
    
    @Override
    public ApplicationResponse processCallback(EppResponse eppResponse) {
        logger.info("Processing callback: orderKey={}, status={}", 
            eppResponse.getOrderKey(), eppResponse.getStatus());
        
        validateEppEnabled();
        validateCallbackRequest(eppResponse);
        
        try {
            String rawResponse = objectMapper.writeValueAsString(eppResponse);
            EppTransaction transaction = processCallbackTransaction(eppResponse, rawResponse);
            
            ApplicationResponse response = createCallbackResponse(eppResponse, transaction);
            response.setMessage("Payment processed successfully");
            return response;
        } catch (JsonProcessingException e) {
            throw new PaymentProcessingException("SERIALIZATION_ERROR", 
                "Failed to serialize callback", e);
        } catch (PaymentProcessingException e) {
            throw e;
        } catch (Exception e) {
            throw new PaymentProcessingException("CALLBACK_PROCESSING_FAILED", 
                "Callback processing failed", e);
        }
    }
    
    public boolean isEppEnabled() {
        return "epp".equalsIgnoreCase(eppProperties.getProvider());
    }
    
    private void validateEppEnabled() {
        if (!isEppEnabled()) {
            throw new PaymentProcessingException("EPP_DISABLED", 
                "EPP integration is not enabled (provider=" + eppProperties.getProvider() + ")");
        }
    }
    
    private void validatePaymentRequest(SaleDetails saleDetails) {
        if (saleDetails == null) {
            throw new PaymentProcessingException("INVALID_REQUEST", "Payment request cannot be null");
        }
        
        if (saleDetails.getOrderKey() == null || saleDetails.getOrderKey().trim().isEmpty()) {
            throw new PaymentProcessingException("INVALID_ORDER_KEY", "Order key is required");
        }
        
        // applicationUniqueId is optional per Commerce Hub requirements
        // No validation required for applicationUniqueId
        
        if (saleDetails.getTotalAmount() == null || saleDetails.getTotalAmount().signum() <= 0) {
            throw new PaymentProcessingException("INVALID_AMOUNT", "Total amount must be greater than zero");
        }
        
        logger.debug("Payment request validation passed for orderKey: {}", saleDetails.getOrderKey());
    }
    
    private void validateCallbackRequest(EppResponse eppResponse) {
        if (eppResponse == null) {
            throw new PaymentProcessingException("INVALID_CALLBACK", "Callback response cannot be null");
        }
        
        if (eppResponse.getOrderKey() == null || eppResponse.getOrderKey().trim().isEmpty()) {
            throw new PaymentProcessingException("INVALID_ORDER_KEY", "Order key is required in callback");
        }
        
        // applicationUniqueId is optional per Commerce Hub requirements
        // No validation required for applicationUniqueId in callback
        
        if (eppResponse.getStatus() == null || eppResponse.getStatus().trim().isEmpty()) {
            throw new PaymentProcessingException("INVALID_STATUS", "Status is required in callback");
        }
        
        logger.debug("Callback request validation passed for orderKey: {}", eppResponse.getOrderKey());
    }
    
    /**
     * Prepares sale details with proper application codes and item keys.
     */
    private void prepareSaleDetails(SaleDetails saleDetails) {
        // applicationUniqueId is optional per Commerce Hub requirements
        // Only set if specifically requested (legacy compatibility)
        if (saleDetails.getApplicationUniqueId() != null && 
            (saleDetails.getApplicationUniqueId().equals("CHANGE_ME") ||
             saleDetails.getApplicationUniqueId().equals("RUC_APP_CODE_FROM_EPP"))) {
            String appCode = getConfiguredAppCode();
            saleDetails.setApplicationUniqueId(appCode);
            logger.info("Set applicationUniqueId to configured RUC app code: {}", appCode);
        }
        
        // Set applicationCode if not provided
        if (saleDetails.getApplicationCode() == null || saleDetails.getApplicationCode().trim().isEmpty()) {
            String appCode = getConfiguredAppCode();
            saleDetails.setApplicationCode(appCode);
            logger.info("Set applicationCode to configured RUC app code: {}", appCode);
        }
        
        // Set itemKey equal to orderKey per Commerce Hub requirements
        if (saleDetails.getItems() != null) {
            saleDetails.getItems().forEach(item -> {
                if (item.getItemKey() == null || item.getItemKey().trim().isEmpty()) {
                    item.setItemKey(saleDetails.getOrderKey());
                    logger.debug("Set itemKey to orderKey: {} for item: {}", 
                               saleDetails.getOrderKey(), item.getDescription());
                }
            });
        }
    }
    
    /**
     * Gets the configured application code from properties.
     */
    private String getConfiguredAppCode() {
        return eppProperties.getApplicationCode() != null ? 
               eppProperties.getApplicationCode() : eppProperties.getAppCode();
    }
    
    /**
     * Handles initial transaction creation/update.
     */
    private void handleInitialTransaction(SaleDetails saleDetails, String rawRequest) {
        if (transactionService != null) {
            EppTransaction transaction = transactionService.createOrUpdateTransaction(
                    saleDetails.getOrderKey(),
                    saleDetails.getApplicationUniqueId(),
                    "APP", // Initial status
                    saleDetails.getTotalAmount(),
                    saleDetails.getEmail(),
                    rawRequest,
                    null, // No response yet
                    null, // No auth code yet
                    null  // No reference number yet
            );
            logger.debug("Transaction record created/updated: {}", transaction.getId());
        } else {
            logger.warn("TransactionService is null - transaction will not be persisted");
        }
    }
    
    /**
     * Processes callback transaction update.
     */
    private EppTransaction processCallbackTransaction(EppResponse eppResponse, String rawResponse) {
        if (transactionService == null) {
            logger.warn("TransactionService is null - cannot retrieve existing transaction");
            return null;
        }
        
        EppTransaction existingTransaction = findExistingTransaction(eppResponse);
        
        EppTransaction updatedTransaction = transactionService.createOrUpdateTransaction(
                eppResponse.getOrderKey(),
                eppResponse.getApplicationUniqueId(),
                eppResponse.getStatus(),
                existingTransaction != null ? existingTransaction.getAmount() : null,
                existingTransaction != null ? existingTransaction.getEmail() : null,
                existingTransaction != null ? existingTransaction.getRawRequest() : null,
                rawResponse,
                eppResponse.getAuthCode(),
                eppResponse.getReferenceNo()
        );
        
        logger.debug("Transaction updated with callback data: {}", updatedTransaction.getId());
        return updatedTransaction;
    }
    
    /**
     * Finds existing transaction safely.
     */
    private EppTransaction findExistingTransaction(EppResponse eppResponse) {
        try {
            return transactionService.findTransaction(
                    eppResponse.getOrderKey(), 
                    eppResponse.getApplicationUniqueId());
        } catch (Exception e) {
            logger.warn("Failed to find existing transaction for orderKey: {}, continuing with callback processing", 
                      eppResponse.getOrderKey(), e);
            return null;
        }
    }
    
    /**
     * Creates callback response from transaction or fallback.
     */
    private ApplicationResponse createCallbackResponse(EppResponse eppResponse, EppTransaction updatedTransaction) {
        if (updatedTransaction != null && modelMapper != null) {
            return modelMapper.toApplicationResponse(updatedTransaction);
        }
        
        // Fallback response when transaction service is unavailable
        ApplicationResponse response = new ApplicationResponse();
        response.setOrderKey(eppResponse.getOrderKey());
        response.setApplicationUniqueId(eppResponse.getApplicationUniqueId());
        response.setStatus(eppResponse.getStatus());
        // Note: ApplicationResponse may not have setAuthCode/setReferenceNo methods
        // These fields might be handled differently in the DTO structure
        
        logger.warn("Created fallback response due to missing transaction service or model mapper");
        return response;
    }
}
