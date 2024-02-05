package com.ShaikZubair.PaymentService.service;

import com.ShaikZubair.PaymentService.model.PaymentRequest;

public interface PaymentService {
    long doPayment(PaymentRequest request);
}
