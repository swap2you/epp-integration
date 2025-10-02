package com.ruc.payments.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "ruc.payments")
public class EppProperties {
    private String provider;
    private String applicationCode; // EPP specification field
    private String appCode; // Legacy field for backward compatibility
    private String paymentGatewayUrl; // EPP specification field
    private String paymentGatewayAPI; // EPP specification field
    private String paymentGatewayIndexUrl; // Legacy field
    private String paymentGatewayApiUrl; // Legacy field
    private String returnUrl;
    private String environment;
    private String merchantId;
    private String hashAlgorithm;
    private boolean encryptionEnabled;

    // Getters and Setters
    public String getProvider() { return provider; }
    public void setProvider(String provider) { this.provider = provider; }
    
    public String getApplicationCode() { return applicationCode; }
    public void setApplicationCode(String applicationCode) { this.applicationCode = applicationCode; }
    
    public String getAppCode() { return appCode; }
    public void setAppCode(String appCode) { this.appCode = appCode; }
    
    public String getPaymentGatewayUrl() { return paymentGatewayUrl; }
    public void setPaymentGatewayUrl(String paymentGatewayUrl) { this.paymentGatewayUrl = paymentGatewayUrl; }
    
    public String getPaymentGatewayAPI() { return paymentGatewayAPI; }
    public void setPaymentGatewayAPI(String paymentGatewayAPI) { this.paymentGatewayAPI = paymentGatewayAPI; }
    
    public String getPaymentGatewayIndexUrl() { return paymentGatewayIndexUrl; }
    public void setPaymentGatewayIndexUrl(String paymentGatewayIndexUrl) { this.paymentGatewayIndexUrl = paymentGatewayIndexUrl; }
    
    public String getPaymentGatewayApiUrl() { return paymentGatewayApiUrl; }
    public void setPaymentGatewayApiUrl(String paymentGatewayApiUrl) { this.paymentGatewayApiUrl = paymentGatewayApiUrl; }
    
    public String getReturnUrl() { return returnUrl; }
    public void setReturnUrl(String returnUrl) { this.returnUrl = returnUrl; }
    
    public String getEnvironment() { return environment; }
    public void setEnvironment(String environment) { this.environment = environment; }
    
    public String getMerchantId() { return merchantId; }
    public void setMerchantId(String merchantId) { this.merchantId = merchantId; }
    
    public String getHashAlgorithm() { return hashAlgorithm; }
    public void setHashAlgorithm(String hashAlgorithm) { this.hashAlgorithm = hashAlgorithm; }
    
    public boolean isEncryptionEnabled() { return encryptionEnabled; }
    public void setEncryptionEnabled(boolean encryptionEnabled) { this.encryptionEnabled = encryptionEnabled; }
}
