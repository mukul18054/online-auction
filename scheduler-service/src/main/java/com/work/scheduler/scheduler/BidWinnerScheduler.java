package com.work.scheduler.scheduler;

import com.work.scheduler.service.SchedulerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class BidWinnerScheduler {
    private final SchedulerService schedulerService;

    @Autowired
    public BidWinnerScheduler(SchedulerService schedulerService) {
        this.schedulerService = schedulerService;
    }

    @Scheduled(fixedRate = 60000) // Runs every minute
    public void checkForExpiredBids() {
        schedulerService.processExpiredProducts();
    }
}