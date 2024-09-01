package com.bunnyMax.productTrading.service.impl;

import com.bunnyMax.productTrading.entity.Order;
import com.bunnyMax.productTrading.entity.Reconciliation;
import com.bunnyMax.productTrading.repository.ReconciliationRepository;
import com.bunnyMax.productTrading.service.IMerchantService;
import com.bunnyMax.productTrading.service.IOrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

import static org.mockito.Mockito.*;

class ReconciliationServiceImplTest {

    @Mock
    private ReconciliationRepository reconciliationRepository;

    @Mock
    private IOrderService orderService;

    @Mock
    private IMerchantService merchantService;

    @InjectMocks
    private ReconciliationServiceImpl reconciliationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void reconcile_PerformsReconciliation() {
        LocalDateTime lastReconciliationTime = LocalDateTime.now().minusDays(1);
        Reconciliation lastReconciliation = new Reconciliation();
        lastReconciliation.setReconciliationTime(lastReconciliationTime);
        when(reconciliationRepository.findTopByOrderByIdDesc()).thenReturn(Optional.of(lastReconciliation));

        Map<Long, BigDecimal> lastBalanceMap = new HashMap<>();
        lastBalanceMap.put(1L, BigDecimal.valueOf(100));
        when(reconciliationRepository.findAllByReconciliationTime(lastReconciliationTime))
                .thenReturn(Collections.singletonList(lastReconciliation));

        List<Order> recentOrders = new ArrayList<>();
        Order order = new Order();
        order.setMerchantId(1L);
        order.setTotalPrice(BigDecimal.valueOf(50));
        recentOrders.add(order);
        when(orderService.getOrderByTime(lastReconciliationTime)).thenReturn(recentOrders);

        Map<Long, BigDecimal> currentBalances = new HashMap<>();
        currentBalances.put(1L, BigDecimal.valueOf(150));
        when(merchantService.getMerchantBalances()).thenReturn(currentBalances);

        reconciliationService.reconcile();

        verify(reconciliationRepository).saveAll(anyList());
    }
}