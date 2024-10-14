package com.work.bidding.dto;

import com.work.bidding.model.Bid;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BidDTO {

    private String bidId;
    private String productId;
    private String userId;
    private Double bidAmount;
    private LocalDateTime bidTime;

    public BidDTO(Bid bid) {
        this.bidId = bid.getId();
        this.productId = bid.getProductId();
        this.userId = bid.getUserId();
        this.bidAmount = bid.getBidAmount().doubleValue();
        this.bidTime = bid.getBidTime();
    }

}
