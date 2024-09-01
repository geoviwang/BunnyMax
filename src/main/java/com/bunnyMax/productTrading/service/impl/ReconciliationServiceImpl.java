package com.bunnyMax.productTrading.service.impl;

import com.bunnyMax.productTrading.entity.Order;
import com.bunnyMax.productTrading.entity.Reconciliation;
import com.bunnyMax.productTrading.repository.ReconciliationRepository;
import com.bunnyMax.productTrading.service.IMerchantService;
import com.bunnyMax.productTrading.service.IOrderService;
import com.bunnyMax.productTrading.service.IReconciliationService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ReconciliationServiceImpl implements IReconciliationService {

    private final ReconciliationRepository reconciliationRepository;
    private final IOrderService orderService;
    private final IMerchantService merchantService;

    @Transactional
    @Override
    public void reconcile() {
        LocalDateTime lastReconciliationTime = getLastReconciliationTime();
        Map<Long, BigDecimal> lastBalanceMap = getLastBalanceMap(lastReconciliationTime);

        List<Order> recentOrders = orderService.getOrderByTime(lastReconciliationTime);
        Map<Long, BigDecimal> currentMerchantBalances = merchantService.getMerchantBalances();

        LocalDateTime now = LocalDateTime.now();
        List<Reconciliation> reconciliations = createReconciliations(recentOrders, currentMerchantBalances, lastBalanceMap, now);

        reconciliationRepository.saveAll(reconciliations);
    }

    private LocalDateTime getLastReconciliationTime() {
        return reconciliationRepository.findTopByOrderByIdDesc()
                .map(Reconciliation::getReconciliationTime)
                .orElse(null);
    }

    private Map<Long, BigDecimal> getLastBalanceMap(LocalDateTime lastReconciliationTime) {
        if (lastReconciliationTime == null) {
            return new HashMap<>();
        }
        return reconciliationRepository.findAllByReconciliationTime(lastReconciliationTime).stream()
                .collect(Collectors.toMap(Reconciliation::getMerchantId,
                        r -> {
                            BigDecimal b = r.getMerchantBalance();
                            return b == null ? BigDecimal.ZERO : b;
                        }));
    }

    private List<Reconciliation> createReconciliations(List<Order> orders,
                                                       Map<Long, BigDecimal> currentBalances,
                                                       Map<Long, BigDecimal> lastBalances,
                                                       LocalDateTime reconciliationTime) {
        Map<Long, BigDecimal> revenueByMerchant = orders.stream()
                .collect(Collectors.groupingBy(Order::getMerchantId,
                        Collectors.reducing(BigDecimal.ZERO,
                                Order::getTotalPrice,
                                BigDecimal::add)));

        return revenueByMerchant.entrySet().stream()
                .map(entry -> createReconciliation(entry.getKey(), entry.getValue(),
                        currentBalances, lastBalances, reconciliationTime))
                .collect(Collectors.toList());
    }

    private Reconciliation createReconciliation(Long merchantId, BigDecimal revenue,
                                                Map<Long, BigDecimal> currentBalances,
                                                Map<Long, BigDecimal> lastBalances,
                                                LocalDateTime reconciliationTime) {
        BigDecimal currentBalance = currentBalances.getOrDefault(merchantId, BigDecimal.ZERO);
        BigDecimal lastBalance = lastBalances.getOrDefault(merchantId, BigDecimal.ZERO);
        boolean isCorrect = revenue.compareTo(currentBalance.subtract(lastBalance)) == 0;

        return Reconciliation.builder()
                .merchantId(merchantId)
                .reconciliationTime(reconciliationTime)
                .merchantBalance(currentBalance)
                .revenue(revenue)
                .correct(isCorrect)
                .build();
    }
}
