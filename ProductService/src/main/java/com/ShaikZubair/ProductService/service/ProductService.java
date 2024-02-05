package com.ShaikZubair.ProductService.service;

import com.ShaikZubair.ProductService.model.ProductRequest;
import com.ShaikZubair.ProductService.model.ProductResponse;

public interface ProductService {
    long addProduct(ProductRequest productRequest);

    ProductResponse getProductById(long productId);

    void reduceQuantity(long productId, long quantity);
}
