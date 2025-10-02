package com.ruc.payments.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ruc.payments.config.EppProperties;
import com.ruc.payments.dto.SaleDetails;
import com.ruc.payments.dto.SaleItems;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Arrays;

/**
 * EPP Integration Controller - Multiple Integration Methods
 * Demonstrates 3 different ways to integrate with EPP Commerce Hub
 */
@Controller
@RequestMapping("/test")
public class TestUIController {
    
    private final ObjectMapper objectMapper;
    private final EppProperties eppProperties;
    
    public TestUIController(EppProperties eppProperties) {
        this.objectMapper = new ObjectMapper();
        this.eppProperties = eppProperties;
    }
    
    @GetMapping("/ping")
    @ResponseBody
    public String testPing() {
        return "EPP Integration Controller Active";
    }
    
    @GetMapping({"", "/", "/form"})
    public String showTestForm(Model model) {
        String orderKey = "TEST-ORDER-" + System.currentTimeMillis();
        
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
        
        return "test-form";
    }
    
    /**
     * Method 1: Direct HTML Response (.NET Style)
     * Writes HTML form directly to response and auto-submits
     */
    @PostMapping("/method1-direct")
    public void method1Direct(HttpServletRequest request, HttpServletResponse response) throws Exception {
        SaleDetails saleDetails = buildSaleDetails(request);
        String jsonPayload = objectMapper.writeValueAsString(saleDetails);
        String launchFormString = buildEppForm(jsonPayload);
        
        System.out.println("Launch Form String: " + launchFormString);
        
        response.setContentType("text/html; charset=UTF-8");
        response.getWriter().write(launchFormString);
        response.getWriter().flush();
    }
    
    /**
     * Method 2: Spring Template Integration
     * Uses Thymeleaf template to render EPP form
     */
    @PostMapping("/method2-template")
    public String method2Template(HttpServletRequest request, Model model) throws Exception {
        SaleDetails saleDetails = buildSaleDetails(request);
        String jsonPayload = objectMapper.writeValueAsString(saleDetails);
        String launchFormString = buildEppForm(jsonPayload);
        
        System.out.println("Launch Form String: " + launchFormString);
        
        model.addAttribute("eppForm", launchFormString);
        return "epp-redirect";
    }
    
    /**
     * Method 3: ModelAndView Integration (Legacy Java Style)
     * Uses request attributes and ModelAndView for EPP form
     */
    @PostMapping("/method3-modelview")
    public ModelAndView method3ModelView(HttpServletRequest request) throws Exception {
        SaleDetails saleDetails = buildSaleDetails(request);
        String jsonPayload = objectMapper.writeValueAsString(saleDetails);
        String launchFormString = buildEppForm(jsonPayload);
        
        System.out.println("Launch Form String: " + launchFormString);
        
        request.setAttribute("EPG_GATEWAY_LAUNCH_FORM", launchFormString);
        return new ModelAndView("EpgInvoke");
    }

    private SaleDetails buildSaleDetails(HttpServletRequest request) {
        SaleDetails saleDetails = new SaleDetails();
        saleDetails.setApplicationCode(request.getParameter("applicationCode"));
        saleDetails.setOrderKey(request.getParameter("orderKey"));
        saleDetails.setFirstName(request.getParameter("firstName"));
        saleDetails.setLastName(request.getParameter("lastName"));
        saleDetails.setAddress1(request.getParameter("address1"));
        saleDetails.setAddress2(request.getParameter("address2"));
        saleDetails.setCity(request.getParameter("city"));
        saleDetails.setStateCode(request.getParameter("stateCode"));
        saleDetails.setZipCode(request.getParameter("zipCode"));
        saleDetails.setEmail(request.getParameter("email"));
        saleDetails.setPaymentAccountType("CC");
        
        BigDecimal totalAmount = new BigDecimal(request.getParameter("amount"));
        saleDetails.setTotalAmount(totalAmount);
        
        SaleItems item = new SaleItems();
        item.setCount(1);
        item.setDescription(request.getParameter("description"));
        item.setAmount(totalAmount);
        item.setItemKey(request.getParameter("orderKey"));
        
        saleDetails.setItems(Arrays.asList(item));
        return saleDetails;
    }
    
    private String buildEppForm(String jsonPayload) {
        StringBuilder sb = new StringBuilder();
        sb.append("<form id='__PostForm' name='__PostForm' action='")
          .append(eppProperties.getPaymentGatewayIndexUrl())
          .append("' method='POST'>")
          .append("<input type='hidden' name='saleDetail' value='")
          .append(jsonPayload)
          .append("'/>")
          .append("</form>")
          .append("<script language='javascript'>var v__PostForm=document.__PostForm;v__PostForm.submit();</script>");
        return sb.toString();
    }
}
