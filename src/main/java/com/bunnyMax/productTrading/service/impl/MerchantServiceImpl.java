package com.bunnyMax.productTrading.service.impl;


import com.bunnyMax.productTrading.entity.Merchant;
import com.bunnyMax.productTrading.repository.MerchantRepository;
import com.bunnyMax.productTrading.service.IMerchantService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class MerchantServiceImpl implements IMerchantService {

    private final MerchantRepository merchantRepository;

    @Cacheable(cacheNames = "merchant", key = "#merchantId", condition = "#result != null")
    @Override
    public Merchant getMerchant(Long merchantId) {
        return getMerchantByRepo(merchantId);
    }

    @Cacheable(cacheNames = "merchantBalances", condition = "#result != null")
    @Override
    public Map<Long, BigDecimal> getMerchantBalances() {
        return merchantRepository.findAll().parallelStream()
                .collect(Collectors.toMap(Merchant::getId, Merchant::getBalance));
    }

    @Caching(evict = {
            @CacheEvict(value = "merchantBalances", allEntries = true),
            @CacheEvict(value = "merchant", key = "#merchantId")
    })
    @Transactional
    @Override
    public void addBalance(Long merchantId, BigDecimal amount) {
        Merchant merchant = getMerchantByRepo(merchantId);
        merchant.setBalance(merchant.getBalance().add(amount));
        merchantRepository.save(merchant);
    }

    private Merchant getMerchantByRepo(Long merchantId) {
        return merchantRepository.findById(merchantId)
                .orElseThrow(() -> new RuntimeException("Merchant not found"));
    }

}


