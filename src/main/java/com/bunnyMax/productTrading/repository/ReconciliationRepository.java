package com.bunnyMax.productTrading.repository;

import com.bunnyMax.productTrading.entity.Reconciliation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReconciliationRepository extends JpaRepository<Reconciliation, Long> {

    Optional<Reconciliation> findTopByOrderByIdDesc();

    List<Reconciliation> findAllByReconciliationTime(LocalDateTime time);

}
