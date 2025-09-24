package com.ruk.payments.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Pattern;

/**
 * Utility class for common validation operations.
 * 
 * This class provides reusable validation methods that can be used
 * across different layers of the application.
 */
public class ValidationUtils {
    
    private static final Logger logger = LoggerFactory.getLogger(ValidationUtils.class);
    
    // Email validation pattern
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$"
    );
    
    // Order key pattern (alphanumeric, dashes, underscores)
    private static final Pattern ORDER_KEY_PATTERN = Pattern.compile(
            "^[a-zA-Z0-9_-]{1,200}$"
    );
    
    /**
     * Validates if a string is not null and not empty after trimming.
     * 
     * @param value The string to validate
     * @return true if valid, false otherwise
     */
    public static boolean isNotEmpty(String value) {
        return value != null && !value.trim().isEmpty();
    }
    
    /**
     * Validates email format.
     * 
     * @param email The email to validate
     * @return true if valid email format, false otherwise
     */
    public static boolean isValidEmail(String email) {
        if (!isNotEmpty(email)) {
            return false;
        }
        return EMAIL_PATTERN.matcher(email.trim()).matches();
    }
    
    /**
     * Validates order key format.
     * 
     * @param orderKey The order key to validate
     * @return true if valid format, false otherwise
     */
    public static boolean isValidOrderKey(String orderKey) {
        if (!isNotEmpty(orderKey)) {
            return false;
        }
        return ORDER_KEY_PATTERN.matcher(orderKey.trim()).matches();
    }
    
    /**
     * Validates that a string length is within specified bounds.
     * 
     * @param value The string to validate
     * @param minLength Minimum length (inclusive)
     * @param maxLength Maximum length (inclusive)
     * @return true if length is valid, false otherwise
     */
    public static boolean isValidLength(String value, int minLength, int maxLength) {
        if (value == null) {
            return minLength <= 0;
        }
        
        int length = value.trim().length();
        return length >= minLength && length <= maxLength;
    }
    
    /**
     * Validates payment status values.
     * 
     * @param status The status to validate
     * @return true if valid status, false otherwise
     */
    public static boolean isValidPaymentStatus(String status) {
        if (!isNotEmpty(status)) {
            return false;
        }
        
        String normalizedStatus = status.trim().toUpperCase();
        return "APP".equals(normalizedStatus) ||
               "COM".equals(normalizedStatus) ||
               "CAN".equals(normalizedStatus) ||
               "DEC".equals(normalizedStatus) ||
               "PEN".equals(normalizedStatus) ||
               "SEN".equals(normalizedStatus);
    }
    
    /**
     * Sanitizes a string by removing potentially harmful characters.
     * 
     * @param input The input string
     * @return Sanitized string
     */
    public static String sanitizeString(String input) {
        if (input == null) {
            return null;
        }
        
        // Remove HTML tags and script content
        return input.replaceAll("<[^>]*>", "")
                   .replaceAll("javascript:", "")
                   .replaceAll("vbscript:", "")
                   .replaceAll("onload", "")
                   .replaceAll("onerror", "")
                   .trim();
    }
    
    /**
     * Logs validation failure for debugging purposes.
     * 
     * @param fieldName The name of the field that failed validation
     * @param value The value that failed validation
     * @param reason The reason for validation failure
     */
    public static void logValidationFailure(String fieldName, Object value, String reason) {
        logger.warn("Validation failed for field '{}' with value '{}': {}", 
                   fieldName, value, reason);
    }
}
