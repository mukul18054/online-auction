package com.work.bidding.controller;

import com.work.bidding.dto.BidDTO;
import com.work.bidding.dto.BidRequest;
import com.work.bidding.dto.BidResponse;
import com.work.bidding.model.Bid;
import com.work.bidding.service.BiddingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/auction/bidding")
public class BiddingController {
    private final BiddingService biddingService;

    @Autowired
    public BiddingController(BiddingService biddingService) {
        this.biddingService = biddingService;
    }

    @PostMapping("/addBids")
    public ResponseEntity<Bid> placeBid(@RequestBody BidRequest bidRequest) {
        Bid bid = biddingService.placeBid(bidRequest);
        return new ResponseEntity<>(bid, HttpStatus.CREATED);
    }

    @GetMapping("/winner/{productId}")
    public ResponseEntity<Bid> getWinningBid(@PathVariable String productId) {
        Bid winningBid = biddingService.getWinningBid(productId);
        if (winningBid == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(winningBid, HttpStatus.OK);
    }


    // get bid
    @GetMapping("/getBids/{userId}")
    public ResponseEntity<List<Bid>> getBids(@PathVariable String userId) {
        // Get all bids for the user
        List<Bid> bids = biddingService.getBidsByUserId(userId);

        // Check if any bids were found
        if (bids.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        // Return the list of bids with OK status code
        return new ResponseEntity<>(bids, HttpStatus.OK);
    }

    // delete bid
    @DeleteMapping("/deleteBids")
    public ResponseEntity<Void> deleteBid(@RequestBody BidRequest bidRequest) {
        String bidId = biddingService.getBidId(bidRequest);
        if (biddingService.getBid(bidId).isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        biddingService.deleteBid(bidId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // update bid
    @PutMapping("/updateBid")
    public ResponseEntity<BidRequest> updateBid( @RequestBody BidRequest bidRequest) {
        // Save the updated bid
        biddingService.updateBid(bidRequest);

        return new ResponseEntity<>(bidRequest, HttpStatus.OK);
    }
    // get bids for a product id
    @GetMapping("/getBidsByProductId/{productId}")
    public ResponseEntity<BidResponse> getBidsByProductId(@PathVariable String productId) {
        List<BidDTO> bids = biddingService.getBidsByProductId(productId);
        if (bids.isEmpty()) {
            return new ResponseEntity<>(new BidResponse(), HttpStatus.NOT_FOUND);
        }
        BidResponse bidResponse = new BidResponse(bids);
        return new ResponseEntity<>(bidResponse, HttpStatus.OK);
    }
}