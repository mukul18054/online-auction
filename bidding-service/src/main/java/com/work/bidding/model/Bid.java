package com.work.bidding.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;
//import jakarta.persistence.*;


//@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "bids")
public class Bid {

    public enum BidStatus {
        PLACED, WINNER, PAYMENT_COMPLETED, PAYMENT_FAILED
    }

    @Id
    private String id;
    private String productId;
    private String userId;
    private BigDecimal bidAmount;
    private LocalDateTime bidTime;
    private BidStatus bidStatus;


    // Constructors, getters, and setters

//    public Bid(String productId, String userId, BigDecimal bidAmount) {
//        this.productId = productId;
//        this.userId = userId;
//        this.bidAmount = bidAmount;
//        this.bidTime = LocalDateTime.now();
//    }

    // Getters and setters
}