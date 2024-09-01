package com.bunnyMax.productTrading.controller;

import com.bunnyMax.productTrading.controller.data.request.PageRequest;
import com.bunnyMax.productTrading.controller.data.request.ProductRequest;
import com.bunnyMax.productTrading.controller.data.resp.ProductListResp;
import com.bunnyMax.productTrading.service.IProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/product")
@Tag(name = "product", description = "API about product.")
public class ProductController {

    private final IProductService productService;

    @Operation(
            summary = "Create Product Quantity",
            responses = {
                    @ApiResponse(responseCode = "200")
            }
    )
    @PostMapping("/create")
    public ResponseEntity<String> createProduct(@RequestBody ProductRequest request) {
        productService.createProduct(
                request.getSku(),
                request.getMerchantId(),
                request.getName(),
                request.getPrice(),
                request.getQuantity()
        );
        return ResponseEntity.ok("Product created successfully");
    }

    @Operation(
            summary = "Add Product Quantity",
            responses = {
                    @ApiResponse(responseCode = "200")
            }
    )
    @PutMapping("/{sku}/quantity")
    public ResponseEntity<String> updateQuantity(@PathVariable String sku, @RequestParam int quantity) {
        productService.updateQuantity(sku, quantity);
        return ResponseEntity.ok("Quantity updated successfully");
    }

    @Operation(
            summary = "Add Product Quantity",
            responses = {
                    @ApiResponse(responseCode = "200")
            }
    )
    @GetMapping("/all")
    public ResponseEntity<ProductListResp> getProductList(@RequestParam PageRequest request) {
        ProductListResp resp = productService.getProducts(request.getPageNum(), request.getPageSize());
        return ResponseEntity.ok(resp);
    }
}
