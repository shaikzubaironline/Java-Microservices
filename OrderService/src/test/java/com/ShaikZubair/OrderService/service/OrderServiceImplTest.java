package com.ShaikZubair.OrderService.service;
import com.ShaikZubair.OrderService.entity.Order;
import com.ShaikZubair.OrderService.exception.CustomException;
import com.ShaikZubair.OrderService.external.client.PaymentService;
import com.ShaikZubair.OrderService.external.client.ProductService;
import com.ShaikZubair.OrderService.external.request.PaymentRequest;
import com.ShaikZubair.OrderService.external.response.ProductResponse;
import com.ShaikZubair.OrderService.model.GetOrderDetailsResponse;
import com.ShaikZubair.OrderService.model.OrderRequest;
import com.ShaikZubair.OrderService.model.PaymentMethod;
import com.ShaikZubair.OrderService.repository.OrderRepository;
import jakarta.annotation.security.RunAs;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import java.time.Instant;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;


@SpringBootTest
public class OrderServiceImplTest {


    @Mock
    private OrderRepository orderRepository;
    @Mock
    private ProductService productService;
    @Mock
    private PaymentService paymentService;

    @Captor
    private ArgumentCaptor<Order> argumentCaptor;

    @InjectMocks
    OrderService orderService = new OrderServiceImpl();

    @Test
    @DisplayName("getOrderDetails - success scenario")
    void testWhenGetOrderDetailsSuccess(){

        //mock internal calls
        Order order = getMockOrder();
        when(orderRepository.findById(anyLong())).thenReturn(Optional.of(order));
        ProductResponse productResponse = getMockProductResponse();
        when(productService.getProductById(anyLong())).thenReturn(ResponseEntity.of(Optional.of(productResponse)));
        //make actual call
        ResponseEntity<GetOrderDetailsResponse> orderResponse= orderService.getOrderDetails(1);
        //Assert
        Assertions.assertNotNull(orderResponse);
        Assertions.assertEquals(order.getQuantity(),orderResponse.getBody().getOrderQuantity());
        //verify
        verify(orderRepository, times(1)).findById(anyLong());
        verify(productService, times(1)).getProductById(anyLong());
    }

    @Test
    @DisplayName("getOrderDetails - failure scenario")
    void testWhenGetOrderDetailsFailure(){
        //Mock
        when(orderRepository.findById(anyLong())).thenReturn(Optional.ofNullable(null));
        //assert
        CustomException exception= Assertions.assertThrows(CustomException.class, ()-> orderService.getOrderDetails(1));
        Assertions.assertEquals("NOT_FOUND",exception.getErrorCode());
        Assertions.assertEquals(404,exception.getStatus());
        //verify
        verify(orderRepository, times(1)).findById(anyLong());
    }

    @Test
    @DisplayName("placeOrder- success scenario")
    void placeOrderSuccessScenario(){
        //mock
        Order order = getMockOrder();
        when(orderRepository.save(any(Order.class))).thenReturn(order);
        when(productService.reduceQuantity(anyLong(), anyLong())).thenReturn(new ResponseEntity<Void>(HttpStatus.OK));
        when(paymentService.doPayment(any(PaymentRequest.class))).thenReturn(new ResponseEntity<Long>(order.getId(),HttpStatus.OK));
        //actual
        OrderRequest request = getMockOrderRequest();
        long orderId = orderService.placeOrder(request);
        //assert
        Assertions.assertEquals(order.getId(), orderId);
        //verify
        verify(productService, times(1)).reduceQuantity(anyLong(), anyLong());
        verify(orderRepository, times(2)).save(any(Order.class));
        verify(paymentService, times(1)).doPayment(any(PaymentRequest.class));
    }

    @Test
    @DisplayName("place Order- Failure Scenario")
    void placeOrderFailureScenario(){
        OrderRequest orderRequest = new OrderRequest(1,1200,2,PaymentMethod.APPLE_PAY);
        CustomException exception = new CustomException("problem doing payment", "PAYMENT_FAILED", 400);
        Order order = new Order(1, 1,1,Instant.now(), "PAYMENT_FAILED", 1200);
        //Mock
        when(productService.reduceQuantity(anyLong(), anyLong())).thenReturn(new ResponseEntity<>(HttpStatusCode.valueOf(400)));
        when(orderRepository.save(argumentCaptor.capture())).thenReturn(order);
        doThrow(exception).when(paymentService).doPayment(any(PaymentRequest.class));
        //actual
        long result =orderService.placeOrder(orderRequest);
        //Assertions
        Mockito.verify(orderRepository, Mockito.times(2)).save(any(Order.class));
        Mockito.verify(productService, Mockito.times(1)).reduceQuantity(anyLong(), anyLong());
        Order capturedOrder = argumentCaptor.getValue();
        Assertions.assertEquals("PAYMENT_FAILED", capturedOrder.getOrderStatus());
        Assertions.assertEquals(1, result);
    }










    OrderRequest getMockOrderRequest(){
        return OrderRequest.builder()
                .paymentMethod(PaymentMethod.APPLE_PAY)
                .productId(1)
                .quantity(1)
                .totalAmount(1200)
                .build();
    }
    Order getMockOrder(){
        return Order.builder()
                .productId(2)
                .quantity(3)
                .orderDate(Instant.now())
                .orderStatus("accepted")
                .amount(823)
                .build();
    }
    ProductResponse getMockProductResponse(){
        return ProductResponse.builder()
                .productName("zubair")
                .price(888)
                .productName("iphone15")
                .quantity(1)
                .build();
    }
}