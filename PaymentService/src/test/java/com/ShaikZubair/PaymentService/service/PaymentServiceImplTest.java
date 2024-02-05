package com.ShaikZubair.PaymentService.service;

import com.ShaikZubair.PaymentService.entity.TransactionDetails;
import com.ShaikZubair.PaymentService.model.PaymentMethod;
import com.ShaikZubair.PaymentService.model.PaymentRequest;
import com.ShaikZubair.PaymentService.repository.TransactionDetailsRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PaymentServiceImplTest {

    @Mock
    TransactionDetailsRepository repository;

    @InjectMocks
    PaymentServiceImpl paymentService;

    @Test
    @DisplayName("doPayment- success scenario")
    void doPaymentTestSuccessScenario(){
        PaymentRequest paymentRequest =
                new PaymentRequest(1,1200, PaymentMethod.APPLE_PAY);
        TransactionDetails transactionDetails =
                new TransactionDetails(1,1,PaymentMethod.APPLE_PAY, Instant.now(),"accepted", 1200);
        //Mocking
        Mockito.when(repository.save(ArgumentMatchers.any(TransactionDetails.class)))
                .thenReturn(transactionDetails);
        //Actual
        long response = paymentService.doPayment(paymentRequest);

        //assertions
        Assertions.assertEquals(transactionDetails.getId(),response);
        //verify
        Mockito.verify(repository,Mockito.times(1))
                .save(ArgumentMatchers.any(TransactionDetails.class));
    }


}