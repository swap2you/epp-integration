package com.ruk.payments.service;

import com.ruk.payments.entity.EppTransaction;

import java.math.BigDecimal;

/**
 * Service interface for transaction management operations.
 * 
 * This interface handles CRUD operations for EPP transactions,
 * providing a layer of abstraction over the repository.
 */
public interface TransactionService {
    
    /**
     * Creates a new transaction or updates an existing one.
     * 
     * @param orderKey The order key
     * @param applicationUniqueId The application unique ID
     * @param status The transaction status
     * @param amount The transaction amount
     * @param email The payer email
     * @param rawRequest The raw request JSON
     * @param rawResponse The raw response JSON
     * @param authCode The authorization code
     * @param referenceNo The reference number
     * @return The created or updated transaction
     */
    EppTransaction createOrUpdateTransaction(
            String orderKey,
            String applicationUniqueId,
            String status,
            BigDecimal amount,
            String email,
            String rawRequest,
            String rawResponse,
            String authCode,
            String referenceNo
    );
    
    /**
     * Finds a transaction by order key and application unique ID.
     * 
     * @param orderKey The order key
     * @param applicationUniqueId The application unique ID
     * @return The transaction if found, null otherwise
     */
    EppTransaction findTransaction(String orderKey, String applicationUniqueId);
    
    /**
     * Finds a transaction by ID.
     * 
     * @param id The transaction ID
     * @return The transaction if found, null otherwise
     */
    EppTransaction findTransactionById(Long id);
}
