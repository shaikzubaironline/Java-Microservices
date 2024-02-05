package com.ShaikZubair.ProductService.controller;

import com.ShaikZubair.ProductService.model.ProductRequest;
import com.ShaikZubair.ProductService.model.ProductResponse;
import com.ShaikZubair.ProductService.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/product")
public class ProductController {
    @Autowired
    private ProductService productService;

    @PostMapping()
    public ResponseEntity<Long> addProduct(@RequestBody ProductRequest productRequest){
        long productId= productService.addProduct(productRequest);
        return new ResponseEntity<>(productId, HttpStatus.CREATED);
    }

    @GetMapping("/{Id}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable("Id") long productId){
        ProductResponse productResponse = productService.getProductById(productId);
        return new ResponseEntity<>(productResponse,HttpStatus.OK);
    }


    @PutMapping("/reduceQuantity/{Id}")
    public ResponseEntity<Void> reduceQuantity(
            @PathVariable("Id") long productId,
            @RequestParam long quantity
    ){
        productService.reduceQuantity(productId, quantity);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
