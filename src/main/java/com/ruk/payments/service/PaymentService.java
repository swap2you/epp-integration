package com.ruk.payments.service;

import com.ruk.payments.dto.ApplicationResponse;
import com.ruk.payments.dto.EppResponse;
import com.ruk.payments.dto.SaleDetails;

/**
 * EPP Payment Service Interface
 */
public interface PaymentService {
    String initiatePayment(SaleDetails saleDetails);
    ApplicationResponse processCallback(EppResponse eppResponse);
}
