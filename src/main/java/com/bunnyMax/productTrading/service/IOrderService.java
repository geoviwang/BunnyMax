package com.bunnyMax.productTrading.service;

import com.bunnyMax.productTrading.entity.Order;
import jakarta.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.List;

public interface IOrderService {
    @Transactional
    Order createOrder(Long userId, Long merchantId, String productSku, int quantity);

    List<Order> getOrderByTime(LocalDateTime lastTime);
}
