package com.ShaikZubair.ProductService.service;

import com.ShaikZubair.ProductService.entity.Product;
import com.ShaikZubair.ProductService.exception.ProductServiceCustomException;
import com.ShaikZubair.ProductService.model.ProductRequest;
import com.ShaikZubair.ProductService.model.ProductResponse;
import com.ShaikZubair.ProductService.repository.ProductRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class ProductServiceImpl implements ProductService{

    @Autowired
    private ProductRepository productRepository;
    @Override
    public long addProduct(ProductRequest productRequest) {
        log.info("logging product");
        Product product = Product.builder()
                .productName(productRequest.getName())
                .quantity(productRequest.getQuantity())
                .price(productRequest.getPrice())
                .build();

        product= productRepository.save(product);
        log.info("product created");
        return product.getProductId();
    }

    @Override
    public ProductResponse getProductById(long productId) {
        log.info("get the product for given Id");
        Product product = productRepository.findById(productId)
                .orElseThrow(()->new ProductServiceCustomException("did not find the product by given id ", "PRODUCT_NOT_FOUND"));

        ProductResponse productResponse= new ProductResponse();
        BeanUtils.copyProperties(product,productResponse);
        return productResponse;
    }

    @Override
    public void reduceQuantity(long productId, long quantity) {
        log.info("reduce quantity {} and quantity {}", productId,quantity);
        Product product = productRepository.findById(productId)
                .orElseThrow(()-> new ProductServiceCustomException("product with given Id not found","PRODUCT_NOT_FOUND"));

        if(product.getQuantity()<quantity){
            throw new ProductServiceCustomException("product does not have enough quantity",
                    "INSUFFICIENT_QUANTITY");
        }

        product.setQuantity(product.getQuantity()-quantity);
        productRepository.save(product);
        log.info("product updated successfully");
    }
}
