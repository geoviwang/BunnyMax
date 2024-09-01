package com.bunnyMax.productTrading.service.impl;

import com.bunnyMax.productTrading.entity.User;
import com.bunnyMax.productTrading.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void addBalance_ValidUser_BalanceAdded() {
        Long userId = 1L;
        BigDecimal initialBalance = BigDecimal.valueOf(100);
        BigDecimal addAmount = BigDecimal.valueOf(50);

        User user = new User();
        user.setId(userId);
        user.setBalance(initialBalance);

        when(userRepository.getReferenceById(userId)).thenReturn(user);

        userService.addBalance(userId, addAmount);

        assertEquals(BigDecimal.valueOf(150), user.getBalance());
        verify(userRepository).save(user);
    }

    @Test
    void deductBalance_SufficientBalance_BalanceDeducted() {
        Long userId = 1L;
        BigDecimal initialBalance = BigDecimal.valueOf(100);
        BigDecimal deductAmount = BigDecimal.valueOf(50);

        User user = new User();
        user.setId(userId);
        user.setBalance(initialBalance);

        when(userRepository.getReferenceById(userId)).thenReturn(user);

        userService.deductBalance(userId, deductAmount);

        assertEquals(BigDecimal.valueOf(50), user.getBalance());
        verify(userRepository).save(user);
    }

    @Test
    void deductBalance_InsufficientBalance_ThrowsException() {
        Long userId = 1L;
        BigDecimal initialBalance = BigDecimal.valueOf(100);
        BigDecimal deductAmount = BigDecimal.valueOf(150);

        User user = new User();
        user.setId(userId);
        user.setBalance(initialBalance);

        when(userRepository.getReferenceById(userId)).thenReturn(user);

        assertThrows(RuntimeException.class, () -> userService.deductBalance(userId, deductAmount));
    }
}