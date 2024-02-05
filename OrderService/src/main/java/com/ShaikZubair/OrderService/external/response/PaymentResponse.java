package com.ShaikZubair.OrderService.external.response;

import com.ShaikZubair.OrderService.model.PaymentMethod;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentResponse {
    private long paymentId;
    private String status;
    private PaymentMethod paymentMode;
    private long amount;
    private Instant paymentDate;
    private long orderId;
}
