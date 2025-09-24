package com.ruk.payments.service.impl;

import com.ruk.payments.entity.EppTransaction;
import com.ruk.payments.repo.EppTransactionRepository;
import com.ruk.payments.service.TransactionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

/**
 * Implementation of TransactionService for managing EPP transactions.
 * 
 * This service handles all transaction-related database operations
 * with proper logging and transaction management.
 */
@Service
@Transactional
public class TransactionServiceImpl implements TransactionService {
    
    private static final Logger logger = LoggerFactory.getLogger(TransactionServiceImpl.class);
    
    private final EppTransactionRepository repository;
    
    public TransactionServiceImpl(EppTransactionRepository repository) {
        this.repository = repository;
    }
    
    @Override
    public EppTransaction createOrUpdateTransaction(
            String orderKey,
            String applicationUniqueId,
            String status,
            BigDecimal amount,
            String email,
            String rawRequest,
            String rawResponse,
            String authCode,
            String referenceNo) {
        
        logger.debug("Creating/updating transaction for orderKey: {}, applicationUniqueId: {}", 
                    orderKey, applicationUniqueId);
        
        // Find existing transaction
        Optional<EppTransaction> existingOpt = repository.findByOrderKeyAndApplicationUniqueId(
                orderKey, applicationUniqueId);
        
        EppTransaction transaction = existingOpt.orElseGet(() -> {
            logger.debug("Creating new transaction for orderKey: {}", orderKey);
            return new EppTransaction();
        });
        
        // Update transaction fields
        transaction.setOrderKey(orderKey);
        transaction.setApplicationUniqueId(applicationUniqueId);
        transaction.setStatus(status);
        
        // Only update non-null values to preserve existing data
        if (amount != null) {
            transaction.setAmount(amount);
        }
        if (email != null) {
            transaction.setEmail(email);
        }
        if (rawRequest != null) {
            transaction.setRawRequest(rawRequest);
        }
        if (rawResponse != null) {
            transaction.setRawResponse(rawResponse);
        }
        if (authCode != null) {
            transaction.setAuthCode(authCode);
        }
        if (referenceNo != null) {
            transaction.setReferenceNo(referenceNo);
        }
        
        EppTransaction savedTransaction = repository.save(transaction);
        
        logger.info("Transaction {} for orderKey: {}, status: {}", 
                   existingOpt.isPresent() ? "updated" : "created", 
                   orderKey, status);
        
        return savedTransaction;
    }
    
    @Override
    public EppTransaction findTransaction(String orderKey, String applicationUniqueId) {
        logger.debug("Finding transaction for orderKey: {}, applicationUniqueId: {}", 
                    orderKey, applicationUniqueId);
        
        return repository.findByOrderKeyAndApplicationUniqueId(orderKey, applicationUniqueId)
                .orElse(null);
    }
    
    @Override
    public EppTransaction findTransactionById(Long id) {
        logger.debug("Finding transaction by ID: {}", id);
        
        return repository.findById(id).orElse(null);
    }
}
