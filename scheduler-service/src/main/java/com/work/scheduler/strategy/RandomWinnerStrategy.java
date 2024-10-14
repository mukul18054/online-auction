package com.work.scheduler.strategy;

import com.work.scheduler.dto.BidDTO;
import com.work.scheduler.dto.ProductDTO;
import com.work.scheduler.dto.WinnerDTO;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Random;

@Component
@Primary
public class RandomWinnerStrategy implements WinnerDeterminationStrategy {
    private final Random random = new Random();

    @Override
    public WinnerDTO determineWinner(ProductDTO product, List<BidDTO> bids) {
        if (bids == null || bids.isEmpty()) {
            return null;
        }
        BidDTO randomBid = bids.get(random.nextInt(bids.size()));
        return new WinnerDTO(randomBid.getUserId(), product.getId(), randomBid.getBidAmount());
    }
}