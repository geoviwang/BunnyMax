package com.bunnyMax.productTrading.service.impl;

import com.bunnyMax.productTrading.entity.Order;
import com.bunnyMax.productTrading.entity.Product;
import com.bunnyMax.productTrading.entity.User;
import com.bunnyMax.productTrading.repository.OrderRepository;
import com.bunnyMax.productTrading.service.IMerchantService;
import com.bunnyMax.productTrading.service.IOrderService;
import com.bunnyMax.productTrading.service.IProductService;
import com.bunnyMax.productTrading.service.IUserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class OrderServiceImpl implements IOrderService {

    private final OrderRepository orderRepository;
    private final IUserService userService;
    private final IMerchantService merchantService;
    private final IProductService productService;

    @Transactional
    @Override
    public Order createOrder(Long userId, Long merchantId, String productSku, int quantity) {
        Product product = productService.getProduct(productSku);
        if (product.getQuantity() < quantity) {
            throw new RuntimeException("Insufficient product quantity.");
        }

        BigDecimal totalPrice = product.getPrice().multiply(BigDecimal.valueOf(quantity));
        userService.deductBalance(userId, totalPrice);
        merchantService.addBalance(merchantId, totalPrice);
        productService.updateQuantity(productSku, -quantity);

        Order order = new Order();
        order.setUserId(userId);
        order.setMerchantId(merchantId);
        order.setProductSku(productSku);
        order.setQuantity(quantity);
        order.setTotalPrice(totalPrice);
        order.setOrderTime(LocalDateTime.now());

        return orderRepository.save(order);
    }

    @Override
    public List<Order> getOrderByTime(LocalDateTime lastTime) {
        return lastTime == null ?
                orderRepository.findAll() :
                orderRepository.findByOrderTimeAfter(lastTime);
    }
}
