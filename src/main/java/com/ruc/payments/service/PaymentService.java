package com.ruc.payments.service;

import com.ruc.payments.dto.ApplicationResponse;
import com.ruc.payments.dto.EppResponse;
import com.ruc.payments.dto.SaleDetails;

/**
 * EPP Payment Service Interface
 */
public interface PaymentService {
    String initiatePayment(SaleDetails saleDetails);
    ApplicationResponse processCallback(EppResponse eppResponse);
}
