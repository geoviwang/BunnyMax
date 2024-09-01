package com.bunnyMax.productTrading.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "`order`")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userId;
    private Long merchantId;
    private String productSku;
    private Integer quantity;
    private BigDecimal totalPrice;
    private LocalDateTime orderTime;

}
