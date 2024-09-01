package com.bunnyMax.productTrading.controller;

import com.bunnyMax.productTrading.service.IUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
@Tag(name = "user", description = "API about user.")
public class UserController {

    private final IUserService userService;

    @Operation(
            summary = "addBalance",
            responses = {
                    @ApiResponse(responseCode = "200")
            }
    )
    @PostMapping("/{userId}/deposit")
    public ResponseEntity<String> addBalance(@PathVariable Long userId, @RequestParam BigDecimal amount) {
        userService.addBalance(userId, amount);
        return ResponseEntity.ok("Deposit successfully.");
    }
}
