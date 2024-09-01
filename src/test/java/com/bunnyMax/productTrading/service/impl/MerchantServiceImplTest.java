package com.bunnyMax.productTrading.service.impl;

import com.bunnyMax.productTrading.entity.Merchant;
import com.bunnyMax.productTrading.repository.MerchantRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.cache.CacheManager;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MerchantServiceImplTest {

    @Mock
    private MerchantRepository merchantRepository;

    @Mock
    private CacheManager cacheManager;

    @InjectMocks
    private MerchantServiceImpl merchantService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getMerchant_ExistingMerchant_ReturnsMerchant() {
        Long merchantId = 1L;
        Merchant merchant = new Merchant();
        merchant.setId(merchantId);
        when(merchantRepository.findById(merchantId)).thenReturn(Optional.of(merchant));

        Merchant result = merchantService.getMerchant(merchantId);

        assertNotNull(result);
        assertEquals(merchantId, result.getId());
    }

    @Test
    void getMerchant_NonExistingMerchant_ThrowsException() {
        Long merchantId = 1L;
        when(merchantRepository.findById(merchantId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> merchantService.getMerchant(merchantId));
    }

    @Test
    void getMerchantBalances_ReturnsBalanceMap() {
        Merchant merchant1 = new Merchant();
        merchant1.setId(1L);
        merchant1.setBalance(BigDecimal.valueOf(100));
        Merchant merchant2 = new Merchant();
        merchant2.setId(2L);
        merchant2.setBalance(BigDecimal.valueOf(200));
        when(merchantRepository.findAll()).thenReturn(Arrays.asList(merchant1, merchant2));

        Map<Long, BigDecimal> result = merchantService.getMerchantBalances();

        assertEquals(2, result.size());
        assertEquals(BigDecimal.valueOf(100), result.get(1L));
        assertEquals(BigDecimal.valueOf(200), result.get(2L));
    }

    @Test
    void addBalance_ExistingMerchant_BalanceAdded() {
        Long merchantId = 1L;
        BigDecimal initialBalance = BigDecimal.valueOf(100);
        BigDecimal addAmount = BigDecimal.valueOf(50);
        Merchant merchant = new Merchant();
        merchant.setId(merchantId);
        merchant.setBalance(initialBalance);
        when(merchantRepository.findById(merchantId)).thenReturn(Optional.of(merchant));

        merchantService.addBalance(merchantId, addAmount);

        assertEquals(BigDecimal.valueOf(150), merchant.getBalance());
        verify(merchantRepository).save(merchant);
    }

    @Test
    void addBalance_NonExistingMerchant_ThrowsException() {
        Long merchantId = 1L;
        BigDecimal addAmount = BigDecimal.valueOf(50);
        when(merchantRepository.findById(merchantId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> merchantService.addBalance(merchantId, addAmount));
    }
}
