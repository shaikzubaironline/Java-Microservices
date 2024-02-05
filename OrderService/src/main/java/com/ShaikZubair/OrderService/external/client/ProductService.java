package com.ShaikZubair.OrderService.external.client;

import com.ShaikZubair.OrderService.external.response.ProductResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "PRODUCT-SERVICE/product")
public interface ProductService {
    @PutMapping("/reduceQuantity/{Id}")
    ResponseEntity<Void> reduceQuantity(
            @PathVariable("Id") long productId,
            @RequestParam long quantity
    );
    @GetMapping("/{Id}")
    ResponseEntity<ProductResponse> getProductById(@PathVariable("Id") long productId);
}
