package com.ruc.payments.controller;

import com.ruc.payments.dto.ApplicationResponse;
import com.ruc.payments.dto.EppResponse;
import com.ruc.payments.dto.SaleDetails;
import com.ruc.payments.exception.PaymentProcessingException;
import com.ruc.payments.service.PaymentService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for EPP payment integration endpoints.
 * 
 * This controller acts as a thin layer that delegates all business logic
 * to the PaymentService. It handles HTTP concerns and basic validation only.
 */
@RestController
@RequestMapping("/payments/epp")
public class PaymentController {
    
    private static final Logger logger = LoggerFactory.getLogger(PaymentController.class);
    
    private final PaymentService paymentService;
    
    /**
     * Constructor for dependency injection.
     */
    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }
    
    /**
     * Health check endpoint.
     * 
     * @return Simple pong response
     */
    @GetMapping("/ping")
    public ResponseEntity<String> ping() {
        logger.debug("Health check requested");
        return ResponseEntity.ok("pong");
    }

    /**
     * Debug endpoint to see JSON serialization output.
     * 
     * @param saleDetails Sale details payload
     * @return JSON string that would be sent to EPP
     */
    @PostMapping("/debug-json")
    public ResponseEntity<String> debugJson(@Valid @RequestBody SaleDetails saleDetails) {
        logger.info("Debug JSON requested for orderKey: {}", saleDetails.getOrderKey());
        
        try {
            com.fasterxml.jackson.databind.ObjectMapper objectMapper = new com.fasterxml.jackson.databind.ObjectMapper();
            String json = objectMapper.writeValueAsString(saleDetails);
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            
            return new ResponseEntity<>(json, headers, HttpStatus.OK);
            
        } catch (Exception e) {
            logger.error("JSON serialization failed", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"error\":\"JSON serialization failed: " + e.getMessage() + "\"}");
        }
    }

    /**
     * Initiates a new EPP payment.
     * 
     * @param saleDetails Sale details payload
     * @return HTML form for EPP hosted checkout
     */
    @PostMapping("/start")
    public ResponseEntity<String> start(@Valid @RequestBody SaleDetails saleDetails) {
        logger.info("Payment start requested for orderKey: {}", saleDetails.getOrderKey());
        
        try {
            String checkoutForm = paymentService.initiatePayment(saleDetails);
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.TEXT_HTML);
            
            return new ResponseEntity<>(checkoutForm, headers, HttpStatus.OK);
            
        } catch (PaymentProcessingException e) {
            logger.error("Payment initiation failed: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Payment initiation failed: " + e.getMessage());
        } catch (Exception e) {
            logger.error("Unexpected error during payment initiation", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An unexpected error occurred");
        }
    }

    /**
     * OnEPPResult method as specified in EPP documentation.
     * This serves as the application API endpoint that EPP calls for post-processing.
     * 
     * @param eppResponse EPPResponse class object from EPP
     * @return ApplicationResponse class object back to EPP with appropriate status
     */
    @PostMapping("/OnEPPResult")
    public ResponseEntity<ApplicationResponse> onEppResult(@Valid @RequestBody EppResponse eppResponse) {
        logger.info("EPP OnEPPResult callback received for orderKey: {}", eppResponse.getOrderKey());
        
        try {
            ApplicationResponse appResponse;
            String status = eppResponse.getStatus();
            
            if ("COM".equals(status)) {
                // Payment completed successfully
                appResponse = paymentService.processCallback(eppResponse);
                logger.info("Payment completed successfully for orderKey: {}", eppResponse.getOrderKey());
                
            } else if ("CAN".equals(status)) {
                // Payment cancelled
                appResponse = paymentService.processCallback(eppResponse);
                // Set status to COM as per EPP specification for cancelled payments
                appResponse.setStatus("COM");
                logger.info("Payment cancelled for orderKey: {}", eppResponse.getOrderKey());
                
            } else {
                // Payment declined or other status
                appResponse = paymentService.processCallback(eppResponse);
                logger.info("Payment status '{}' for orderKey: {}", status, eppResponse.getOrderKey());
            }
            
            return ResponseEntity.ok(appResponse);
            
        } catch (Exception ex) {
            logger.error("OnEPPResult processing failed for orderKey: {}", eppResponse.getOrderKey(), ex);
            
            String statusValue = "CAN".equals(eppResponse.getStatus()) ? "CAN" : "RET";
            
            ApplicationResponse appResponse = new ApplicationResponse();
            appResponse.setOrderKey(eppResponse.getOrderKey());
            appResponse.setApplicationUniqueId(eppResponse.getApplicationUniqueId());
            appResponse.setErrorMessage(ex.getMessage() + System.lineSeparator() + getStackTrace(ex));
            appResponse.setStatus(statusValue);
            
            return ResponseEntity.ok(appResponse);
        }
    }
    
    /**
     * Helper method to get stack trace as string for EPP error reporting.
     */
    private String getStackTrace(Exception ex) {
        java.io.StringWriter sw = new java.io.StringWriter();
        java.io.PrintWriter pw = new java.io.PrintWriter(sw);
        ex.printStackTrace(pw);
        return sw.toString();
    }

    /**
     * Handles EPP callback/result (legacy endpoint for backward compatibility).
     * 
     * @param eppResponse Callback payload from EPP
     * @return Application response with updated status
     */
    @PostMapping("/result")
    public ResponseEntity<ApplicationResponse> result(@Valid @RequestBody EppResponse eppResponse) {
        logger.info("Payment callback received for orderKey: {}", eppResponse.getOrderKey());
        
        try {
            ApplicationResponse response = paymentService.processCallback(eppResponse);
            return ResponseEntity.ok(response);
            
        } catch (PaymentProcessingException e) {
            logger.error("Callback processing failed: {}", e.getMessage());
            
            // Return error response in ApplicationResponse format
            ApplicationResponse errorResponse = new ApplicationResponse();
            errorResponse.setOrderKey(eppResponse.getOrderKey());
            errorResponse.setApplicationUniqueId(eppResponse.getApplicationUniqueId());
            errorResponse.setStatus("ERROR");
            errorResponse.setMessage("Callback processing failed: " + e.getMessage());
            
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
            
        } catch (Exception e) {
            logger.error("Unexpected error during callback processing", e);
            
            // Return error response in ApplicationResponse format
            ApplicationResponse errorResponse = new ApplicationResponse();
            errorResponse.setOrderKey(eppResponse.getOrderKey());
            errorResponse.setApplicationUniqueId(eppResponse.getApplicationUniqueId());
            errorResponse.setStatus("ERROR");
            errorResponse.setMessage("An unexpected error occurred");
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
    
}
