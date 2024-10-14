package com.work.scheduler.strategy;

import com.work.scheduler.dto.BidDTO;
import com.work.scheduler.dto.ProductDTO;
import com.work.scheduler.dto.WinnerDTO;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;

@Component
public class HighestBidWinnerStrategy implements WinnerDeterminationStrategy {
    @Override
    public WinnerDTO determineWinner(ProductDTO product, List<BidDTO> bids) {
        if (bids == null || bids.isEmpty()) {
            return null; // Or return a WinnerDTO with appropriate information for no winner
        }
        return bids.stream()
                .max(Comparator.comparing(BidDTO::getBidAmount))
                .map(highestBid -> new WinnerDTO(highestBid.getUserId(), product.getId(), highestBid.getBidAmount()))
                .orElse(null);
    }
}