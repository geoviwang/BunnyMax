package com.bunnyMax.productTrading.service;

import com.bunnyMax.productTrading.controller.data.resp.ProductListResp;
import com.bunnyMax.productTrading.entity.Product;
import com.bunnyMax.productTrading.vo.ProductVo;

import java.math.BigDecimal;
import java.util.List;

public interface IProductService {

    Product getProduct(String sku);

    void createProduct(String sku, Long merchantId, String name, BigDecimal price, int quality);

    void updateQuantity(String sku, int quantity);

    ProductListResp getProducts(int pageNum, int pageSize);

}
