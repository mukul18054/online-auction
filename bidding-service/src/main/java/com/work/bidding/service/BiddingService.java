package com.work.bidding.service;

import ch.qos.logback.core.util.StringUtil;
import com.work.bidding.dto.BidDTO;
import com.work.bidding.dto.BidRequest;
import com.work.bidding.impl.BiddingServiceImpl;
import com.work.bidding.model.Bid;
import com.work.bidding.repository.BidRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public interface BiddingService {


    public Bid placeBid(BidRequest bidRequest);

    public Bid getWinningBid(String productId);

    public List<Bid> getBidsByUserId(String userId);

    public void deleteBid(String bidId);

    public void updateBid(BidRequest bidRequest);

    public String getBidId(BidRequest bidRequest);

    public Optional<Bid> getBid(String bidId);

    public List<BidDTO> getBidsByProductId(String productId);
}