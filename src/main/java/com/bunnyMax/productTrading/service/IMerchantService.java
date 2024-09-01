package com.bunnyMax.productTrading.service;

import com.bunnyMax.productTrading.entity.Merchant;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface IMerchantService {

    Merchant getMerchant(Long merchantId);

    Map<Long, BigDecimal> getMerchantBalances();

    void addBalance(Long merchantId, BigDecimal amount);
}
