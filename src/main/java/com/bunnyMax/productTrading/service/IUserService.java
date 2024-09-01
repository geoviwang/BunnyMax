package com.bunnyMax.productTrading.service;

import com.bunnyMax.productTrading.entity.User;
import jakarta.transaction.Transactional;

import java.math.BigDecimal;
import java.util.concurrent.TimeUnit;

public interface IUserService {

    void addBalance(Long userId, BigDecimal amount);

    void deductBalance(Long userId, BigDecimal amount);

}
