package com.ShaikZubair.OrderService.service;

import com.ShaikZubair.OrderService.model.GetOrderDetailsResponse;
import com.ShaikZubair.OrderService.model.OrderRequest;
import org.springframework.http.ResponseEntity;

public interface OrderService {

    long placeOrder(OrderRequest orderRequest);
    ResponseEntity<GetOrderDetailsResponse> getOrderDetails(long orderId);
}
