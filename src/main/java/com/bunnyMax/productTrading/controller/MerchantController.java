package com.bunnyMax.productTrading.controller;

import com.bunnyMax.productTrading.entity.Merchant;
import com.bunnyMax.productTrading.service.IMerchantService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/merchant")
@Tag(name = "merchant", description = "API about merchant.")
public class MerchantController {

    private final IMerchantService merchantService;

    @Operation(
            summary = "getBalance",
            responses = {
                    @ApiResponse(responseCode = "200")
            }
    )
    @GetMapping("/{merchantId}/balance")
    public ResponseEntity<BigDecimal> getBalance(@PathVariable Long merchantId) {
        Merchant merchant = merchantService.getMerchant(merchantId);
        return ResponseEntity.ok(merchant.getBalance());
    }
}
