package com.ruk.payments.util;

import com.ruk.payments.dto.ApplicationResponse;
import com.ruk.payments.entity.EppTransaction;
import org.springframework.stereotype.Component;

/**
 * Utility class for mapping between different model types.
 * 
 * This mapper handles conversion between DTOs, entities, and other model objects
 * to keep the conversion logic centralized and reusable.
 */
@Component
public class ModelMapper {
    
    /**
     * Converts an EppTransaction entity to ApplicationResponse DTO.
     * 
     * @param transaction The transaction entity
     * @return The corresponding ApplicationResponse DTO
     */
    public ApplicationResponse toApplicationResponse(EppTransaction transaction) {
        if (transaction == null) {
            return null;
        }
        
        ApplicationResponse response = new ApplicationResponse();
        response.setOrderKey(transaction.getOrderKey());
        response.setApplicationUniqueId(transaction.getApplicationUniqueId());
        response.setStatus(transaction.getStatus());
        
        return response;
    }
    
    /**
     * Converts an EppTransaction entity to ApplicationResponse DTO with custom message.
     * 
     * @param transaction The transaction entity
     * @param message Custom message to include
     * @return The corresponding ApplicationResponse DTO with message
     */
    public ApplicationResponse toApplicationResponse(EppTransaction transaction, String message) {
        ApplicationResponse response = toApplicationResponse(transaction);
        if (response != null) {
            response.setMessage(message);
        }
        return response;
    }
}
