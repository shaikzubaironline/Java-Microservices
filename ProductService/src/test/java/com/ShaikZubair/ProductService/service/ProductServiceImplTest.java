package com.ShaikZubair.ProductService.service;
import com.ShaikZubair.ProductService.entity.Product;
import com.ShaikZubair.ProductService.exception.ProductServiceCustomException;
import com.ShaikZubair.ProductService.model.ProductRequest;
import com.ShaikZubair.ProductService.model.ProductResponse;
import com.ShaikZubair.ProductService.repository.ProductRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.Optional;
import static org.mockito.Mockito.*;

@SpringBootTest
public class ProductServiceImplTest {

    @Mock
    ProductRepository productRepository;

    @Captor
    ArgumentCaptor<Product> argumentCaptor;

    @InjectMocks
    ProductServiceImpl productService;

    @Test
    @DisplayName("add product - success scenario")
    void addProductSuccessScenario(){
        ProductRequest request = new ProductRequest("iphone", 1200L, 1L);
        Product product = new Product(1, "iphone", 1200, 1);
        //Mock
        when(productRepository.save(any(Product.class))).thenReturn(product);
        //Actual
        long productId= productService.addProduct(request);
        //Assertions
        Assertions.assertEquals(1, productId);
        //verify
        Mockito.verify(productRepository,times(1)).save(any(Product.class));
    }

    @Test
    @DisplayName("get product by id - success scenario")
    void getProductByIdSuccessScenario(){
        Product product = new Product(1,"Iphone", 1200, 12);
        //mock
        when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));
        //actual
        ProductResponse productResponse = productService.getProductById(1L);
        //assertions
        Assertions.assertEquals("Iphone", productResponse.getProductName());
        Assertions.assertEquals(1, productResponse.getProductId());
        Assertions.assertEquals(12, productResponse.getQuantity());
        Assertions.assertEquals(1200, productResponse.getPrice());
    }

    @Test
    @DisplayName("get Product by Id - Failure Scenario")
    void getProductByIdFailureScenario(){
        //mock
        when(productRepository.findById(anyLong())).thenReturn(Optional.empty());
        //actual
        ProductServiceCustomException exception = Assertions.assertThrows(
                ProductServiceCustomException.class,
                ()-> {
            productService.getProductById(1L);
        });
        //assertions
        Assertions.assertEquals("PRODUCT_NOT_FOUND", exception.getErrorCode());
    }

    @Test
    @DisplayName("reduce Quantity - success scenario")
    void reduceQuantitySuccessScenario(){
        Product product1 = new Product(1,"Iphone", 1200,2);
        Product product2 = new Product(1,"Iphone", 1200,1);

        //mock
        when(productRepository.findById(anyLong())).thenReturn(Optional.of(product1));
        when(productRepository.save(argumentCaptor.capture())).thenReturn(product2);
        //actual
        productService.reduceQuantity(1,1);
        //verify
        Product product = argumentCaptor.getValue();
        Assertions.assertEquals(1, product.getQuantity());
        verify(productRepository, times(1)).findById(anyLong());
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    @DisplayName("reduce Quantity - failure - Product not found by Id")
    void reduceQuantityFailureProductNotFound(){
        //mock
        when(productRepository.findById(anyLong())).thenReturn(Optional.empty());

        ProductServiceCustomException exception = Assertions.assertThrows(ProductServiceCustomException.class, ()->{
            productService.reduceQuantity(1,1);
        });

        Assertions.assertEquals("PRODUCT_NOT_FOUND", exception.getErrorCode());
        verify(productRepository , times(1)).findById(anyLong());
    }

    @Test
    @DisplayName("reduce quantity - failure - Insufficient quantity")
    void reduceQuantityFailureInsufficientQuantity(){
        Product product1 = new Product(1,"Iphone", 1200,1);
        //mock
        when(productRepository.findById(anyLong())).thenReturn(Optional.of(product1));
        //actual
        ProductServiceCustomException exception = Assertions.assertThrows(ProductServiceCustomException.class, ()->{
            productService.reduceQuantity(1,2);
        });
        //assert
        Assertions.assertEquals("INSUFFICIENT_QUANTITY" , exception.getErrorCode());
        verify(productRepository, times(1)).findById(anyLong());
    }
}