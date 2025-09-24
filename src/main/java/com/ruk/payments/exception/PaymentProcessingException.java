package com.ruk.payments.exception;

/**
 * Custom exception for payment processing errors.
 * 
 * This exception is thrown when payment operations fail due to business logic
 * or technical issues during payment processing.
 */
public class PaymentProcessingException extends RuntimeException {
    
    private final String errorCode;
    
    public PaymentProcessingException(String message) {
        super(message);
        this.errorCode = "PAYMENT_ERROR";
    }
    
    public PaymentProcessingException(String message, Throwable cause) {
        super(message, cause);
        this.errorCode = "PAYMENT_ERROR";
    }
    
    public PaymentProcessingException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }
    
    public PaymentProcessingException(String errorCode, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }
    
    public String getErrorCode() {
        return errorCode;
    }
}
