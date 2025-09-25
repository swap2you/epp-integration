package com.ruk.payments.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ruk.payments.dto.SaleDetails;
import com.ruk.payments.dto.SaleItems;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Arrays;

/**
 * Simple UI controller for EPP testing.
 * Serves HTML forms and handles submissions without CORS issues.
 * 
 * Supports both Spring template-based and direct .NET-style response approaches.
 */
@Controller
@RequestMapping("/test")
public class TestUIController {
    
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    
    // Toggle between Spring template approach (false) and direct .NET-style response (true)
    @Value("${epp.test.use-direct-response:false}")
    private boolean useDirectResponse;
    
    public TestUIController() {
        System.out.println("🏗️ TestUIController CONSTRUCTOR called!");
        this.restTemplate = new RestTemplate();
        this.objectMapper = new ObjectMapper();
        System.out.println("✅ TestUIController initialized with RestTemplate and ObjectMapper");
    }
    
    /**
     * Simple test endpoint to verify controller is working.
     */
    @GetMapping("/ping")
    @ResponseBody
    public String testPing() {
        System.out.println("🏓 /test/ping endpoint hit!");
        return "UI Controller is working!";
    }
    
    /**
     * Show the simple test form.
     */
    @GetMapping({"", "/", "/form"})
    public String showTestForm(Model model) {
        System.out.println("📋 /test form page requested - showTestForm() called");
        // Pre-populate with test data
        String orderKey = "TEST-ORDER-" + System.currentTimeMillis();
        System.out.println("🔑 Generated Order Key: " + orderKey);
        
        model.addAttribute("applicationCode", "3256d54a-9e63-4c7d-b2f9-a2897ec82aab");
        model.addAttribute("orderKey", orderKey);
        model.addAttribute("firstName", "John");
        model.addAttribute("lastName", "Smith");
        model.addAttribute("address1", "400 Market Street");
        model.addAttribute("city", "Harrisburg");
        model.addAttribute("stateCode", "PA");
        model.addAttribute("zipCode", "17111");
        model.addAttribute("email", "testuser@gmail.com");
        model.addAttribute("amount", "100.00");
        model.addAttribute("description", "RUC Payment Test");
        
        System.out.println("✅ Form pre-populated, returning test-form template");
        return "test-form";
    }
    
    /**
     * Handle form submission and redirect to EPP.
     * Supports both Spring template approach and direct .NET-style response.
     */
    @PostMapping("/submit")
    public String submitTest(
            @RequestParam("applicationCode") String applicationCode,
            @RequestParam("orderKey") String orderKey,
            @RequestParam("firstName") String firstName,
            @RequestParam("lastName") String lastName,
            @RequestParam("address1") String address1,
            @RequestParam(value = "address2", required = false) String address2,
            @RequestParam("city") String city,
            @RequestParam("stateCode") String stateCode,
            @RequestParam("zipCode") String zipCode,
            @RequestParam("email") String email,
            @RequestParam("amount") String amount,
            @RequestParam("description") String description,
            Model model,
            HttpServletResponse response) throws IOException {
        
        System.out.println("==========================================================");
        System.out.println("🚀 SUBMIT ENDPOINT HIT! /test/submit");
        System.out.println("==========================================================");
        System.out.println("✅ Form Data Received:");
        System.out.println("  - Application Code: " + applicationCode);
        System.out.println("  - Order Key: " + orderKey);
        System.out.println("  - First Name: " + firstName);
        System.out.println("  - Last Name: " + lastName);
        System.out.println("  - Address1: " + address1);
        System.out.println("  - Address2: " + address2);
        System.out.println("  - City: " + city);
        System.out.println("  - State: " + stateCode);
        System.out.println("  - ZIP: " + zipCode);
        System.out.println("  - Email: " + email);
        System.out.println("  - Amount: " + amount);
        System.out.println("  - Description: " + description);
        System.out.println("==========================================================");
        
        try {
            System.out.println("🔄 Building SaleDetails object...");
            // Build SaleDetails from form data
            SaleDetails saleDetails = new SaleDetails();
            saleDetails.setApplicationCode(applicationCode);
            saleDetails.setOrderKey(orderKey);
            saleDetails.setFirstName(firstName);
            saleDetails.setLastName(lastName);
            saleDetails.setAddress1(address1);
            saleDetails.setAddress2(address2);
            saleDetails.setCity(city);
            saleDetails.setStateCode(stateCode);
            saleDetails.setZipCode(zipCode);
            saleDetails.setEmail(email);
            saleDetails.setPaymentAccountType("CC");
            
            BigDecimal totalAmount = new BigDecimal(amount);
            saleDetails.setTotalAmount(totalAmount);
            System.out.println("💰 Total Amount parsed: " + totalAmount);
            
            // Create sale item
            SaleItems item = new SaleItems();
            item.setCount(1);
            item.setDescription(description);
            item.setAmount(totalAmount);
            item.setItemKey(orderKey);
            
            saleDetails.setItems(Arrays.asList(item));
            System.out.println("📦 Sale Items created: " + saleDetails.getItems().size() + " items");
            
            // Log the request for debugging
            System.out.println("=== READY TO CALL EPP ENDPOINT ===");
            System.out.println("Order Key: " + orderKey);
            System.out.println("Application Code: " + applicationCode);
            System.out.println("Total Amount: " + totalAmount);
            System.out.println("Items: " + saleDetails.getItems().size());
            
            // Make HTTP call to /payments/epp/start endpoint
            System.out.println("🌐 Preparing HTTP call to /payments/epp/start...");
            HttpHeaders headers = new HttpHeaders();
            // use text/html content type (EPP endpoint now supports both)
            headers.setContentType(MediaType.parseMediaType("text/html; charset=UTF-8"));
            headers.setAccept(Arrays.asList(MediaType.TEXT_HTML));
            System.out.println("📋 Content-Type: text/html; charset=UTF-8 (as requested by Rahul)");

            String requestBody = objectMapper.writeValueAsString(saleDetails);
            System.out.println("📤 JSON Request Body: " + requestBody);
            System.out.println("🔗 Calling URL: http://localhost:8080/payments/epp/start");
            
            HttpEntity<String> request = new HttpEntity<>(requestBody, headers);
            
            System.out.println("⏳ Making HTTP POST request...");
            ResponseEntity<String> httpResponse = restTemplate.postForEntity(
                "http://localhost:8080/payments/epp/start", 
                request, 
                String.class
            );
            
            System.out.println("✅ HTTP Response received!");
            System.out.println("📊 Response Status: " + httpResponse.getStatusCode());
            System.out.println("📋 Response Headers: " + httpResponse.getHeaders());
            
            String eppForm = httpResponse.getBody();
            System.out.println("📄 EPP Form HTML length: " + (eppForm != null ? eppForm.length() : 0));
            if (eppForm != null && eppForm.length() > 0) {
                System.out.println("🎯 EPP Form Preview (first 200 chars): " + 
                    (eppForm.length() > 200 ? eppForm.substring(0, 200) + "..." : eppForm));
            }
            System.out.println("EPP Form generated successfully, length: " + (eppForm != null ? eppForm.length() : 0));
            
            // Toggle between approaches based on configuration
            if (useDirectResponse) {
                // .NET-style approach: Direct response writing
                return handleDirectResponse(eppForm, response);
            } else {
                // Spring template approach: Use Thymeleaf template
                return handleTemplateResponse(eppForm, model);
            }
            
        } catch (Exception e) {
            System.err.println("=== ERROR IN TEST UI SUBMISSION ===");
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
            
            // Add error to model and return to form
            model.addAttribute("error", "Error processing payment: " + e.getMessage());
            model.addAttribute("applicationCode", applicationCode);
            model.addAttribute("orderKey", orderKey);
            model.addAttribute("firstName", firstName);
            model.addAttribute("lastName", lastName);
            model.addAttribute("address1", address1);
            model.addAttribute("address2", address2);
            model.addAttribute("city", city);
            model.addAttribute("stateCode", stateCode);
            model.addAttribute("zipCode", zipCode);
            model.addAttribute("email", email);
            model.addAttribute("amount", amount);
            model.addAttribute("description", description);
            
            return "test-form";
        }
    }

    /**
     * Handle direct response approach (.NET-style).
     * Writes HTML directly to the HTTP response, similar to .NET HttpContext.Response.WriteAsync.
     */
    private String handleDirectResponse(String eppForm, HttpServletResponse response) throws IOException {
        System.out.println("🔧 Using DIRECT RESPONSE approach (.NET-style)");
        
        // Set content type exactly like .NET: "text/html; charset=UTF-8"
        response.setContentType("text/html; charset=UTF-8");
        
        // Write HTML directly to response (like .NET HttpContext.Response.WriteAsync)
        response.getWriter().write(eppForm);
        response.getWriter().flush();
        
        System.out.println("✅ HTML written directly to response");
        return null; // Return null to indicate we handled the response directly
    }

    /**
     * Handle template response approach (Spring-style).
     * Uses Thymeleaf template to render the form.
     */
    private String handleTemplateResponse(String eppForm, Model model) {
        System.out.println("🔧 Using TEMPLATE RESPONSE approach (Spring-style)");
        
        // Add the EPP form to model for template rendering
        model.addAttribute("eppForm", eppForm);
        
        System.out.println("✅ EPP form added to model for template rendering");
        return "epp-redirect"; // Return template name
    }
}