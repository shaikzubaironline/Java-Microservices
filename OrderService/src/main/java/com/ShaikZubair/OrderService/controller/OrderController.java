package com.ShaikZubair.OrderService.controller;

import com.ShaikZubair.OrderService.model.GetOrderDetailsResponse;
import com.ShaikZubair.OrderService.model.OrderRequest;
import com.ShaikZubair.OrderService.service.OrderService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/order")
@Log4j2
public class OrderController {
    @Autowired
    private OrderService orderService;

    @PostMapping("/placeOrder")
    public ResponseEntity<Long> PlaceOrder(@RequestBody OrderRequest orderRequest){
        log.info("PlaceOrder api called");
        long orderId = orderService.placeOrder(orderRequest);
        log.info("Order Id {}", orderId);
        return new ResponseEntity<>(orderId, HttpStatus.OK);
    }


    @GetMapping("/orderDetails/{orderId}")
    public ResponseEntity<GetOrderDetailsResponse> getOrderDetails(@PathVariable(name = "orderId") long orderId){
        return orderService.getOrderDetails(orderId);
    }
}
