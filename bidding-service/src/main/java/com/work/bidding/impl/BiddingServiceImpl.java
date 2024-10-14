package com.work.bidding.impl;
import ch.qos.logback.core.util.StringUtil;
import com.work.bidding.dto.BidDTO;
import com.work.bidding.dto.BidRequest;
import com.work.bidding.model.Bid;
import com.work.bidding.repository.BidRepository;
import com.work.bidding.service.BiddingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BiddingServiceImpl implements BiddingService {


    private final BidRepository bidRepository;


    @Autowired
    public BiddingServiceImpl(BidRepository bidRepository) {
        this.bidRepository = bidRepository;
    }

    public Bid placeBid(BidRequest bidRequest) {
        // Here you would typically check if the bid is valid (e.g., higher than base price)
        // For simplicity, we're skipping those checks
        Bid bid = BiddingServiceImpl.getBid(bidRequest);
        return bidRepository.insert(bid);
    }

    public Bid getWinningBid(String productId) {
        List<Bid> bids = bidRepository.findByProductIdOrderByBidAmountDesc(productId);
        return bids.isEmpty() ? null : bids.get(0);
    }

    public List<Bid> getBidsByUserId(String userId) {
        return bidRepository.findActiveBidsByUserId(userId);
    }

    public void deleteBid(String bidId) {
        bidRepository.deleteById(bidId);
    }

    public void updateBid(BidRequest bidRequest) {
        // Retrieve the existing bid based on bidId
        String bidId = getBidId(bidRequest);
        Optional<Bid> existingBid = getBid(bidId);

        // Check if the bid exists
        if (existingBid.isEmpty()) {
            // throw exception if bid does not exist
//            throw new RuntimeException("Bid does not exist");
            throw new IllegalArgumentException("Bid does not exist");
        }

        // check if bid amount is less than existing bid amount

        if (bidRequest.getBidAmount().compareTo(existingBid.get().getBidAmount()) < 0) { // just check will his last instead of total max bid amount
            throw new IllegalArgumentException("Bid amount is less than existing bid amount");
        }
        // Update the bid amount
        existingBid.get().setBidAmount(bidRequest.getBidAmount());
        bidRepository.save(existingBid.get());
    }

    public String getBidId(BidRequest bidRequest) {
        return constructBidId(bidRequest);
    }

    public Optional<Bid> getBid(String bidId) {
        return bidRepository.findById(bidId);
    }

    public List<BidDTO> getBidsByProductId(String productId) {
        List<Bid> bids = bidRepository.findByProductIdOrderByBidAmountDesc(productId);
        List<BidDTO> bidDTOS = new ArrayList<>();
        bids.forEach(bid -> bidDTOS.add(new BidDTO(bid)));
        return bidDTOS;
    }
    public static Bid getBid(BidRequest bidRequest) {
        return Bid.builder()
                .id(constructBidId(bidRequest))
                .productId(bidRequest.getProductId())
                .bidAmount(bidRequest.getBidAmount())
                .userId(bidRequest.getUserId())
                .bidTime(LocalDateTime.now())
                .bidStatus(Bid.BidStatus.PLACED)
                .build();
    }

    public static String constructBidId(BidRequest bidRequest) {
        // throw exception if productId or userId is null
        if (StringUtil.isNullOrEmpty(bidRequest.getProductId()) || StringUtil.isNullOrEmpty(bidRequest.getUserId())){
            throw new IllegalArgumentException("productId or userId cannot be null or empty");
        }

        return bidRequest.getProductId()+":"+bidRequest.getUserId();
    }
}
