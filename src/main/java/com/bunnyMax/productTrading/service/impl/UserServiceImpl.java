package com.bunnyMax.productTrading.service.impl;

import com.bunnyMax.productTrading.entity.User;
import com.bunnyMax.productTrading.repository.UserRepository;
import com.bunnyMax.productTrading.service.IUserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements IUserService {

    private final UserRepository userRepository;


    @Transactional
    @Override
    public void addBalance(Long userId, BigDecimal amount) {
        User user = getUser(userId);
        user.setBalance(user.getBalance().add(amount));
        userRepository.save(user);
    }

    @Transactional
    @Override
    public void deductBalance(Long userId, BigDecimal amount) {
        User user = getUser(userId);
        if (user.getBalance().compareTo(amount) < 0) {
            throw new RuntimeException("Insufficient balance.");
        }
        user.setBalance(user.getBalance().subtract(amount));
        userRepository.save(user);
    }

    private User getUser(Long userId) {
        return userRepository.getReferenceById(userId);
    }
}
