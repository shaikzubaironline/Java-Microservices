package com.ShaikZubair.OrderService.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetOrderDetailsResponse {
    String orderStatus;
    String productName;
    long productPrice;
    long orderQuantity;
    Instant orderDate;

}
