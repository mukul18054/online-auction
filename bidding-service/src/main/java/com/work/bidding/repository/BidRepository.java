package com.work.bidding.repository;

import com.work.bidding.model.Bid;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BidRepository extends MongoRepository<Bid, String> {
    //bids with the same bidAmount will be sorted based on their insertion order (first-come-first-served, or FCFS).
    List<Bid> findByProductIdOrderByBidAmountDesc(String productId); //


    @Query(value = "{ userId: ?0 }")
    public List<Bid> findActiveBidsByUserId(String userId);

//    @Query("SELECT b FROM Bid b WHERE b.userId = :userId")
//    public List<Bid> findActiveBidsByUserId(String userId);
}