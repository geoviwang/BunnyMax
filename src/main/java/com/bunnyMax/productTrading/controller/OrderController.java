package com.bunnyMax.productTrading.controller;

import com.bunnyMax.productTrading.controller.data.request.OrderRequest;
import com.bunnyMax.productTrading.entity.Order;
import com.bunnyMax.productTrading.service.IOrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final IOrderService orderService;

    @Operation(
            summary = "createOrder",
            responses = {
                    @ApiResponse(responseCode = "200")
            }
    )
    @PostMapping
    public ResponseEntity<Order> createOrder(@RequestBody OrderRequest orderRequest) {
        Order order = orderService.createOrder(
                orderRequest.getUserId(),
                orderRequest.getMerchantId(),
                orderRequest.getProductSku(),
                orderRequest.getQuantity()
        );
        return ResponseEntity.ok(order);
    }
}
