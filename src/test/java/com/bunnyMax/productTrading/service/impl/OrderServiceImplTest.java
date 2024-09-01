package com.bunnyMax.productTrading.service.impl;

import com.bunnyMax.productTrading.entity.Order;
import com.bunnyMax.productTrading.entity.Product;
import com.bunnyMax.productTrading.repository.OrderRepository;
import com.bunnyMax.productTrading.service.IMerchantService;
import com.bunnyMax.productTrading.service.IProductService;
import com.bunnyMax.productTrading.service.IUserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrderServiceImplTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private IUserService userService;

    @Mock
    private IMerchantService merchantService;

    @Mock
    private IProductService productService;

    @InjectMocks
    private OrderServiceImpl orderService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createOrder_ValidOrder_OrderCreated() {
        Long userId = 1L;
        Long merchantId = 2L;
        String productSku = "SKU123";
        int quantity = 2;
        BigDecimal price = BigDecimal.valueOf(10);
        BigDecimal totalPrice = price.multiply(BigDecimal.valueOf(quantity));

        Product product = new Product();
        product.setSku(productSku);
        product.setPrice(price);
        product.setQuantity(5);

        when(productService.getProduct(productSku)).thenReturn(product);
        when(orderRepository.save(any(Order.class))).thenAnswer(i -> i.getArguments()[0]);

        Order result = orderService.createOrder(userId, merchantId, productSku, quantity);

        assertNotNull(result);
        assertEquals(userId, result.getUserId());
        assertEquals(merchantId, result.getMerchantId());
        assertEquals(productSku, result.getProductSku());
        assertEquals(quantity, result.getQuantity());
        assertEquals(totalPrice, result.getTotalPrice());

        verify(userService).deductBalance(userId, totalPrice);
        verify(merchantService).addBalance(merchantId, totalPrice);
        verify(productService).updateQuantity(productSku, -quantity);
    }

    @Test
    void createOrder_InsufficientQuantity_ThrowsException() {
        Long userId = 1L;
        Long merchantId = 2L;
        String productSku = "SKU123";
        int quantity = 10;

        Product product = new Product();
        product.setSku(productSku);
        product.setQuantity(5);

        when(productService.getProduct(productSku)).thenReturn(product);

        assertThrows(RuntimeException.class, () -> orderService.createOrder(userId, merchantId, productSku, quantity));
    }

    @Test
    void getOrderByTime_WithLastTime_ReturnsFilteredOrders() {
        LocalDateTime lastTime = LocalDateTime.now().minusDays(1);
        List<Order> expectedOrders = Arrays.asList(new Order(), new Order());
        when(orderRepository.findByOrderTimeAfter(lastTime)).thenReturn(expectedOrders);

        List<Order> result = orderService.getOrderByTime(lastTime);

        assertEquals(expectedOrders, result);
    }

    @Test
    void getOrderByTime_WithoutLastTime_ReturnsAllOrders() {
        List<Order> expectedOrders = Arrays.asList(new Order(), new Order(), new Order());
        when(orderRepository.findAll()).thenReturn(expectedOrders);

        List<Order> result = orderService.getOrderByTime(null);

        assertEquals(expectedOrders, result);
    }
}