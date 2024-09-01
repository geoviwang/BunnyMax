package com.bunnyMax.productTrading.repository;

import com.bunnyMax.productTrading.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByOrderTimeAfter(LocalDateTime lastTime);

}
