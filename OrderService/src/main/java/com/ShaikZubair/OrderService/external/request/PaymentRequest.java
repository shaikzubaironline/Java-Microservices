package com.ShaikZubair.OrderService.external.request;

import com.ShaikZubair.OrderService.model.PaymentMethod;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentRequest {
    private long orderId;
    private long amount;
    private PaymentMethod paymentMethod;
}
