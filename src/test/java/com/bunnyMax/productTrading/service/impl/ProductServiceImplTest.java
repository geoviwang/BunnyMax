package com.bunnyMax.productTrading.service.impl;

import com.bunnyMax.productTrading.controller.data.resp.ProductListResp;
import com.bunnyMax.productTrading.entity.Product;
import com.bunnyMax.productTrading.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getProduct_ExistingProduct_ReturnsProduct() {
        String sku = "SKU123";
        Product product = new Product();
        product.setSku(sku);
        when(productRepository.findById(sku)).thenReturn(Optional.of(product));

        Product result = productService.getProduct(sku);

        assertNotNull(result);
        assertEquals(sku, result.getSku());
    }

    @Test
    void getProduct_NonExistingProduct_ReturnsNull() {
        String sku = "SKU123";
        when(productRepository.findById(sku)).thenReturn(Optional.empty());

        Product result = productService.getProduct(sku);

        assertNull(result);
    }

    @Test
    void createProduct_NewProduct_ProductCreated() {
        String sku = "SKU123";
        Long merchantId = 1L;
        String name = "Test Product";
        BigDecimal price = BigDecimal.valueOf(10);
        int quantity = 100;

        when(productRepository.findById(sku)).thenReturn(Optional.empty());

        productService.createProduct(sku, merchantId, name, price, quantity);

        verify(productRepository).save(any(Product.class));
    }

    @Test
    void createProduct_ExistingProduct_ThrowsException() {
        String sku = "SKU123";
        when(productRepository.findById(sku)).thenReturn(Optional.of(new Product()));

        assertThrows(RuntimeException.class, () -> productService.createProduct(sku, 1L, "Test", BigDecimal.TEN, 100));
    }

    @Test
    void updateQuantity_ExistingProduct_QuantityUpdated() {
        String sku = "SKU123";
        Product product = new Product();
        product.setSku(sku);
        product.setQuantity(100);
        when(productRepository.findById(sku)).thenReturn(Optional.of(product));

        productService.updateQuantity(sku, -10);

        assertEquals(90, product.getQuantity());
        verify(productRepository).save(product);
    }

    @Test
    void updateQuantity_NonExistingProduct_ThrowsException() {
        String sku = "SKU123";
        when(productRepository.findById(sku)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> productService.updateQuantity(sku, 10));
    }

    @Test
    void getProducts_ReturnsProductListResp() {
        int pageNum = 1;
        int pageSize = 10;
        Page<Product> productPage = new PageImpl<>(Arrays.asList(new Product(), new Product()));
        when(productRepository.findAll(any(PageRequest.class))).thenReturn(productPage);

        ProductListResp result = productService.getProducts(pageNum, pageSize);

        assertNotNull(result);
        assertEquals(2, result.getTotal());
        assertEquals(pageNum, result.getPageNum());
        assertEquals(pageSize, result.getPageSize());
        assertEquals(2, result.getProductList().size());
    }
}
