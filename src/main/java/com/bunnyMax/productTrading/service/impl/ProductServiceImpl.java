package com.bunnyMax.productTrading.service.impl;

import com.bunnyMax.productTrading.controller.data.resp.ProductListResp;
import com.bunnyMax.productTrading.entity.Product;
import com.bunnyMax.productTrading.repository.ProductRepository;
import com.bunnyMax.productTrading.service.IProductService;
import com.bunnyMax.productTrading.vo.ProductVo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ProductServiceImpl  implements IProductService {

    private final ProductRepository productRepository;

    @Override
    public Product getProduct(String sku) {
        return productRepository.findById(sku).orElse(null);
    }

    @CacheEvict(cacheNames = "products", allEntries = true)
    @Override
    public void createProduct(String sku, Long merchantId, String name, BigDecimal price, int quality) {
        Product product = getProduct(sku);
        if(product != null) {
            throw new RuntimeException("Sku already exists.");
        }
        product = new Product();
        product.setSku(sku);
        product.setMerchantId(merchantId);
        product.setName(name);
        product.setPrice(price);
        product.setQuantity(quality);
        productRepository.save(product);
    }

    @CacheEvict(cacheNames = "products", allEntries = true)
    @Transactional
    @Override
    public void updateQuantity(String sku, int quantity) {
        Product product = getProduct(sku);
        if(product == null) {
            throw new RuntimeException("Product not found.");
        }
        product.setQuantity(product.getQuantity() + quantity);
        productRepository.save(product);
    }

    @Cacheable(cacheNames = "products", key = "#pageNum + '_' + #pageSize")
    @Override
    public ProductListResp getProducts(int pageNum, int pageSize) {
        Pageable pageable = PageRequest.of(pageNum, pageNum);
        Page<Product> products = productRepository.findAll(pageable);
        ProductListResp resp = new ProductListResp();
        resp.setTotal(products.getTotalElements());
        resp.setPageNum(pageNum);
        resp.setCurrentSize(products.getSize());
        resp.setPageSize(pageSize);
        resp.setTotalPage(products.getTotalPages());
        resp.setProductList(products.get().map(ProductVo::new).collect(Collectors.toList()));
        return resp;
    }
}
