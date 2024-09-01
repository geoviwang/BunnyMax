package com.bunnyMax.productTrading.controller.data.request;

import lombok.Data;

@Data
public class OrderRequest {
    private Long userId;
    private Long merchantId;
    private String productSku;
    private int quantity;

}
