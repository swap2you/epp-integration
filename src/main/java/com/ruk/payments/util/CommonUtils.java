package com.ruk.payments.util;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

/**
 * Utility class for common operations across the application.
 * 
 * This class provides reusable utility methods for formatting,
 * generation, and other common operations.
 */
public class CommonUtils {

    // Private constructor to prevent instantiation
    private CommonUtils() {
        throw new UnsupportedOperationException("Utility class");
    }
    
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
    
    /**
     * Generates a unique reference number.
     * 
     * @return A unique reference number
     */
    public static String generateReferenceNumber() {
        return "REF-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
    
    /**
     * Generates a unique transaction ID.
     * 
     * @return A unique transaction ID
     */
    public static String generateTransactionId() {
        return "TXN-" + UUID.randomUUID().toString().substring(0, 12).toUpperCase();
    }
    
    /**
     * Formats a timestamp to ISO string format.
     * 
     * @param timestamp The timestamp to format
     * @return Formatted timestamp string
     */
    public static String formatTimestamp(OffsetDateTime timestamp) {
        if (timestamp == null) {
            return null;
        }
        return timestamp.format(DATE_FORMATTER);
    }
    
    /**
     * Gets current timestamp as formatted string.
     * 
     * @return Current timestamp as string
     */
    public static String getCurrentTimestampString() {
        return formatTimestamp(OffsetDateTime.now());
    }
    
    /**
     * Masks sensitive data for logging purposes.
     * 
     * @param data The data to mask
     * @param visibleChars Number of characters to keep visible
     * @return Masked string
     */
    public static String maskSensitiveData(String data, int visibleChars) {
        if (data == null || data.length() <= visibleChars) {
            return "***";
        }
        
        if (visibleChars <= 0) {
            return repeatChar('*', data.length());
        }
        
        String visible = data.substring(0, Math.min(visibleChars, data.length()));
        String masked = repeatChar('*', Math.max(0, data.length() - visibleChars));
        return visible + masked;
    }
    
    /**
     * Masks email for logging purposes.
     * 
     * @param email The email to mask
     * @return Masked email
     */
    public static String maskEmail(String email) {
        if (email == null || !email.contains("@")) {
            return "***@***.***";
        }
        
        String[] parts = email.split("@");
        if (parts.length != 2) {
            return "***@***.***";
        }
        
        String localPart = parts[0];
        String domainPart = parts[1];
        
        String maskedLocal = localPart.length() <= 2 ? "***" : 
                           localPart.substring(0, 2) + repeatChar('*', localPart.length() - 2);
        
        String maskedDomain = domainPart.length() <= 3 ? "***" :
                            domainPart.substring(0, 1) + repeatChar('*', domainPart.length() - 3) + 
                            domainPart.substring(domainPart.length() - 2);
        
        return maskedLocal + "@" + maskedDomain;
    }
    
    /**
     * Truncates a string to specified length with ellipsis.
     * 
     * @param text The text to truncate
     * @param maxLength Maximum length
     * @return Truncated text
     */
    public static String truncateWithEllipsis(String text, int maxLength) {
        if (text == null || text.length() <= maxLength) {
            return text;
        }
        
        if (maxLength <= 3) {
            return "...";
        }
        
        return text.substring(0, maxLength - 3) + "...";
    }

    /**
     * Utility method to repeat a character n times.
     * 
     * @param c The character to repeat
     * @param count Number of times to repeat
     * @return String with repeated character
     */
    private static String repeatChar(char c, int count) {
        if (count <= 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder(count);
        for (int i = 0; i < count; i++) {
            sb.append(c);
        }
        return sb.toString();
    }
    
    /**
     * Checks if a string represents a boolean true value.
     * 
     * @param value The string value
     * @return true if represents true, false otherwise
     */
    public static boolean isTrueValue(String value) {
        if (value == null) {
            return false;
        }
        
        String normalized = value.trim().toLowerCase();
        return "true".equals(normalized) || 
               "yes".equals(normalized) || 
               "1".equals(normalized) ||
               "on".equals(normalized);
    }
}
