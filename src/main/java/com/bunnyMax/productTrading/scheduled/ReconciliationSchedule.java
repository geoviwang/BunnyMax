package com.bunnyMax.productTrading.scheduled;

import com.bunnyMax.productTrading.service.IReconciliationService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class ReconciliationSchedule {

    private final IReconciliationService reconciliationService;

    @Scheduled(cron = "0 0 0 * * ?") // Run daily at midnight
    @Transactional
    public void reconcile() {
        reconciliationService.reconcile();
    }

}
