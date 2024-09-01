package com.bunnyMax.productTrading.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
@Data
@Entity
@Table(name = "reconciliation")
@RequiredArgsConstructor
@AllArgsConstructor
public class Reconciliation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime reconciliationTime;
    private Long merchantId;
    private BigDecimal merchantBalance;
    private BigDecimal revenue;
    private boolean correct;

}
