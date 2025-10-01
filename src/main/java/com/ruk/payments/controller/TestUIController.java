package com.ruk.payments.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ruk.payments.config.EppProperties;
import com.ruk.payments.dto.SaleDetails;
import com.ruk.payments.dto.SaleItems;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

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
    private final EppProperties eppProperties;
    
    // Toggle between Spring template approach (false) and direct .NET-style response (true)
    @Value("${epp.test.use-direct-response:false}")
    private boolean useDirectResponse;
    
    public TestUIController(EppProperties eppProperties) {
        System.out.println("🏗️ TestUIController CONSTRUCTOR called!");
        this.restTemplate = new RestTemplate();
        this.objectMapper = new ObjectMapper();
        this.eppProperties = eppProperties;
        System.out.println("✅ TestUIController initialized with RestTemplate, ObjectMapper, and EppProperties");
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
        Model model) throws IOException {
        
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
            System.out.println("📋 Content-Type: text/html; charset=UTF-8");

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

            System.out.println("------------------ EPP FORM -------------------------------------");
            System.out.println(eppForm);
            System.out.println("-----------------------------------------------------------------");

            if (eppForm != null && eppForm.length() > 0) {
                System.out.println("🎯 EPP Form Preview (first 200 chars): " + 
                    (eppForm.length() > 200 ? eppForm.substring(0, 200) + "..." : eppForm));
            }
            System.out.println("EPP Form generated successfully, length: " + (eppForm != null ? eppForm.length() : 0));
            
            // Toggle between approaches based on configuration
            // Always return a view name to avoid 404s from null returns
            // If direct-response is toggled on, we still use the template to render the HTML safely.
            return handleTemplateResponse(eppForm, model);
            
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
     * Handle direct response approach (.NET-style) without returning null.
     * To avoid 404 Whitelabel issues, we route through the Thymeleaf template instead of writing directly.
     */
    private String handleDirectResponse(String eppForm, Model model) {
        System.out.println("🔧 Using DIRECT RESPONSE approach (.NET-style) via template to avoid 404");
        model.addAttribute("eppForm", eppForm);
        return "epp-redirect";
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

    /**
     * Rahul's approach: Handle form submission using the old Java project pattern.
     * This follows the EpgInvoke.htm pattern with ModelAndView and request attributes.
     */
    @PostMapping("/rahul-submit")
    public ModelAndView rahulSubmitTest(
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
            HttpServletRequest request) throws Exception {
        
        System.out.println("==========================================================");
        System.out.println("🎯 RAHUL'S METHOD ENDPOINT HIT! /test/rahul-submit");
        System.out.println("==========================================================");
        
        // Build SaleDetails object (same as regular submit)
        SaleDetails saleDetails = buildSaleDetails(applicationCode, orderKey, firstName, lastName, 
                address1, address2, city, stateCode, zipCode, email, amount, description);
        
        // Rahul's approach: Get JSON for sale detail
        String postData = getJSONForSaleDetail(saleDetails);
        System.out.println("📄 JSON for EPG: " + postData);
        
        // Rahul's approach: Build the launch form string
        String url = eppProperties.getPaymentGatewayIndexUrl(); // Use configured Gateway URL
        String launchFormString = JSONBuildPostForm(url, postData);
        System.out.println("🚀 Launch Form String: " + launchFormString);
        
        // Rahul's approach: Set as request attribute
        request.setAttribute("EPG_GATEWAY_LAUNCH_FORM", launchFormString);
        System.out.println("✅ EPG_GATEWAY_LAUNCH_FORM attribute set for EpgInvoke template");
        
        // Return ModelAndView for EpgInvoke template (following Rahul's pattern)
        return new ModelAndView("EpgInvoke");
    }

    /**
     * Helper method: Get JSON for sale detail (Rahul's EPGHelper.getJSONForSaleDetail equivalent)
     */
    private String getJSONForSaleDetail(SaleDetails saleDetails) throws Exception {
        return objectMapper.writeValueAsString(saleDetails);
    }

    /**
     * Helper method: Build POST form from JSON data (Rahul's EPGHelper.JSONBuildPostForm equivalent)
     */
    private String JSONBuildPostForm(String url, String jsonPostData) {
        System.out.println("🔧 Building POST form using Rahul's JSONBuildPostForm approach");
        
        StringBuilder formBuilder = new StringBuilder();
        formBuilder.append("<form id='__PostForm' name='__PostForm' action='").append(escapeForHtml(url)).append("' method='POST'>")
                   .append("<input type='hidden' name='saleDetail' value='").append(escapeForHtml(jsonPostData)).append("'/>")
                //    .append("<input type='hidden' name='returnUrl' value='").append(escapeForHtml(eppProperties.getReturnUrl())).append("'/>")
                   .append("</form>")
                   .append("<script language='javascript'>var v__PostForm=document.__PostForm;v__PostForm.submit();</script>");
        
        return formBuilder.toString();
    }

    /**
     * Helper method: Build SaleDetails object (shared between both approaches)
     */
    private SaleDetails buildSaleDetails(String applicationCode, String orderKey, String firstName, String lastName,
                                       String address1, String address2, String city, String stateCode, String zipCode,
                                       String email, String amount, String description) {
        System.out.println("🔨 Building SaleDetails object...");
        
        SaleDetails saleDetails = new SaleDetails();
        saleDetails.setApplicationCode(applicationCode);
        saleDetails.setOrderKey(orderKey);
        saleDetails.setFirstName(firstName);
        saleDetails.setLastName(lastName);
        saleDetails.setAddress1(address1);
        saleDetails.setAddress2(address2 != null ? address2 : "");
        saleDetails.setCity(city);
        saleDetails.setStateCode(stateCode);
        saleDetails.setZipCode(zipCode);
        saleDetails.setEmail(email);
        saleDetails.setPaymentAccountType("CC");
        
        BigDecimal totalAmount = new BigDecimal(amount);
        saleDetails.setTotalAmount(totalAmount);
        
        // Create sale item
        SaleItems item = new SaleItems();
        item.setCount(1);
        item.setDescription(description);
        item.setAmount(totalAmount);
        item.setItemKey(orderKey);
        
        saleDetails.setItems(Arrays.asList(item));
        
        return saleDetails;
    }

    /**
     * Helper method: Escape HTML for form values
     */
    private String escapeForHtml(String value) {
        if (value == null) return "";
        // return value
        //         .replace("&", "&amp;")
        //         .replace("\"", "&quot;")
        //         .replace("'", "&#39;")
        //         .replace("<", "&lt;")
        //         .replace(">", "&gt;");
        return value;
    }
}