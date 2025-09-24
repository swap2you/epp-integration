package com.ruk.payments.service;

import com.ruk.payments.dto.ApplicationResponse;
import com.ruk.payments.dto.EppResponse;
import com.ruk.payments.dto.SaleDetails;

/**
 * Service interface for EPP payment processing business logic.
 * 
 * This interface defines the contract for EPP payment operations,
 * separating business logic from the controller layer.
 */
public interface PaymentService {
    
    /**
     * Initiates a new EPP payment process.
     * 
     * @param saleDetails The payment details to process
     * @return HTML form for EPP hosted checkout
     * @throws PaymentProcessingException if payment initiation fails
     */
    String initiatePayment(SaleDetails saleDetails);
    
    /**
     * Processes EPP callback/result from payment gateway.
     * 
     * @param eppResponse The callback response from EPP
     * @return Application response with updated status
     * @throws PaymentProcessingException if callback processing fails
     */
    ApplicationResponse processCallback(EppResponse eppResponse);
    
    /**
     * Checks if EPP provider is enabled and available.
     * 
     * @return true if EPP is enabled, false otherwise
     */
    boolean isEppEnabled();
}
