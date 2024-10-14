package com.work.scheduler.strategy;

import com.work.scheduler.dto.BidDTO;
import com.work.scheduler.dto.ProductDTO;
import com.work.scheduler.dto.WinnerDTO;

import java.util.List;

public interface WinnerDeterminationStrategy {
    WinnerDTO determineWinner(ProductDTO product, List<BidDTO> bids);
}