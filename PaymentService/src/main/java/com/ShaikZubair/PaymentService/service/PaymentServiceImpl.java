package com.ShaikZubair.PaymentService.service;

import com.ShaikZubair.PaymentService.entity.TransactionDetails;
import com.ShaikZubair.PaymentService.model.PaymentRequest;
import com.ShaikZubair.PaymentService.repository.TransactionDetailsRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@Log4j2
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private TransactionDetailsRepository repository;
    @Override
    public long doPayment(PaymentRequest request) {
        log.info("payment request service layer called");

        TransactionDetails details = TransactionDetails.builder()
                .amount(request.getAmount())
                .paymentDate(Instant.now())
                .paymentMethod(request.getPaymentMethod())
                .paymentStatus("SUCCESS")
                .orderId(request.getOrderId())
                .build();

        TransactionDetails detailsUpdated = repository.save(details);
        return detailsUpdated.getId();
    }
}
