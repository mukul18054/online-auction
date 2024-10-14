
package com.work.product.repository;

import com.work.product.dto.ProductDTO;
import com.work.product.model.Product;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface ProductRepository extends MongoRepository<Product, String> {
    List<Product> findByCategoryId(String categoryId);
    List<Product> findByBiddingStartTimeBeforeAndBiddingEndTimeAfter(LocalDateTime start, LocalDateTime end);

    @Query(value = "{ biddingEndTime: { $lt: ?0 }, status: 'ACTIVE' }")
    List<Product> findProductsBeforeNowAndActive(LocalDateTime now);

}
