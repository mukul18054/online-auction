package com.work.bidding.dto;

import com.mongodb.lang.Nullable;

import java.math.BigDecimal;

public class BidRequest {
    private String productId;
    private String userId;

    @Nullable
    private BigDecimal bidAmount;

    // Constructors
    public BidRequest() {}

    public BidRequest(String productId, String userId, BigDecimal bidAmount) {
        this.productId = productId;
        this.userId = userId;
        this.bidAmount = bidAmount;
    }

    // Getters and setters
    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public BigDecimal getBidAmount() {
        return bidAmount;
    }

    public void setBidAmount(BigDecimal bidAmount) {
        this.bidAmount = bidAmount;
    }
}