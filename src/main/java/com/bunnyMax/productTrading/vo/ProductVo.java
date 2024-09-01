package com.bunnyMax.productTrading.vo;

import com.bunnyMax.productTrading.entity.Product;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductVo extends Product {


    public ProductVo(Product product) {
        super(product.getSku(),
                product.getMerchantId(),
                product.getName(),
                product.getPrice(),
                product.getQuantity());
    }
}
