package com.work.scheduler.impl;

import com.work.scheduler.client.BiddingClient;
import com.work.scheduler.client.ProductClient;
import com.work.scheduler.dto.BidDTO;
import com.work.scheduler.dto.ProductDTO;
import com.work.scheduler.dto.WinnerDTO;
import com.work.scheduler.producer.KafkaProducer;
import com.work.scheduler.service.SchedulerService;
import com.work.scheduler.strategy.WinnerDeterminationStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SchedulerServiceImpl implements SchedulerService {
    private final ProductClient productClient;
    private final BiddingClient biddingClient;
    private final KafkaProducer kafkaProducer;
    private final WinnerDeterminationStrategy winnerDeterminationStrategy;

    @Override
    public void processExpiredProducts() {
        List<ProductDTO> expiredProducts = productClient.getExpiredProducts();
        for (ProductDTO product : expiredProducts) {
            processExpiredProduct(product);
        }
    }

    @Override
    @Async
    public void processExpiredProduct(ProductDTO product) {
        List<BidDTO> bids = biddingClient.getBidsForProduct(product.getId()).getBids();
        WinnerDTO winner = winnerDeterminationStrategy.determineWinner(product, bids);
        if (winner != null) {
            kafkaProducer.sendWinnerNotification(winner);
        }
    }
}