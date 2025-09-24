package com.ruk.payments.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ruk.payments.config.EppProperties;
import com.ruk.payments.dto.ApplicationResponse;
import com.ruk.payments.dto.EppResponse;
import com.ruk.payments.dto.SaleDetails;
import com.ruk.payments.entity.EppTransaction;
import com.ruk.payments.exception.PaymentProcessingException;
import com.ruk.payments.service.EppClient;
import com.ruk.payments.service.PaymentService;
import com.ruk.payments.service.TransactionService;
import com.ruk.payments.util.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of PaymentService that handles EPP payment processing.
 * 
 * This service contains all business logic for payment operations,
 * including validation, conversion, and transaction management.
 */
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
        logger.info("Initiating payment for orderKey: {}, applicationUniqueId: {}", 
                   saleDetails.getOrderKey(), saleDetails.getApplicationUniqueId());
        
        validateEppEnabled();
        validatePaymentRequest(saleDetails);
        
        // applicationUniqueId is optional per Commerce Hub requirements
        // Only set if specifically requested (legacy compatibility)
        if (saleDetails.getApplicationUniqueId() != null && 
            (saleDetails.getApplicationUniqueId().equals("CHANGE_ME") ||
             saleDetails.getApplicationUniqueId().equals("RUC_APP_CODE_FROM_EPP"))) {
            String appCode = eppProperties.getApplicationCode() != null ? 
                           eppProperties.getApplicationCode() : eppProperties.getAppCode();
            saleDetails.setApplicationUniqueId(appCode);
            logger.info("Set applicationUniqueId to configured RUC app code: {}", appCode);
        }
        
        // Set applicationCode if not provided
        if (saleDetails.getApplicationCode() == null || saleDetails.getApplicationCode().trim().isEmpty()) {
            String appCode = eppProperties.getApplicationCode() != null ? 
                           eppProperties.getApplicationCode() : eppProperties.getAppCode();
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
        
        try {
            // Convert to JSON for storage (commented out since transactionService is not implemented)
            // String rawRequest = objectMapper.writeValueAsString(saleDetails);
            
            // Create/update transaction record
            // EppTransaction transaction = transactionService.createOrUpdateTransaction(
            //         saleDetails.getOrderKey(),
            //         saleDetails.getApplicationUniqueId(),
            //         "APP", // Initial status
            //         saleDetails.getTotalAmount(),
            //         saleDetails.getEmail(),
            //         rawRequest,
            //         null, // No response yet
            //         null, // No auth code yet
            //         null  // No reference number yet
            // );
            
            // Generate hosted checkout form
            String checkoutForm = eppClient.buildHostedCheckoutForm(saleDetails);
            
            logger.info("Payment initiated successfully for orderKey: {}", saleDetails.getOrderKey());
            return checkoutForm;
            
        } catch (Exception e) {
            logger.error("Payment initiation failed for orderKey: {}", saleDetails.getOrderKey(), e);
            throw new PaymentProcessingException("PAYMENT_INITIATION_FAILED", 
                "Failed to initiate payment", e);
        }
    }
    
    @Override
    public ApplicationResponse processCallback(EppResponse eppResponse) {
        logger.info("Processing callback for orderKey: {}, status: {}", 
                   eppResponse.getOrderKey(), eppResponse.getStatus());
        
        validateEppEnabled();
        validateCallbackRequest(eppResponse);
        
        try {
            // Convert to JSON for storage
            String rawResponse = objectMapper.writeValueAsString(eppResponse);
            
            // Find existing transaction to get amount and email
            EppTransaction existingTransaction = transactionService.findTransaction(
                    eppResponse.getOrderKey(), 
                    eppResponse.getApplicationUniqueId());
            
            // Update transaction with callback data
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
            
            // Convert to response model
            ApplicationResponse response = modelMapper.toApplicationResponse(updatedTransaction);
            response.setMessage("Payment callback processed successfully");
            
            logger.info("Callback processed successfully for orderKey: {}", eppResponse.getOrderKey());
            return response;
            
        } catch (JsonProcessingException e) {
            logger.error("Failed to serialize callback response for orderKey: {}", eppResponse.getOrderKey(), e);
            throw new PaymentProcessingException("SERIALIZATION_ERROR", 
                "Failed to process callback response", e);
        } catch (Exception e) {
            logger.error("Callback processing failed for orderKey: {}", eppResponse.getOrderKey(), e);
            throw new PaymentProcessingException("CALLBACK_PROCESSING_FAILED", 
                "Failed to process payment callback", e);
        }
    }
    
    @Override
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
}
