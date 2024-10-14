
package com.work.product.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.work.product.model.Product;
import lombok.*;


//@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDTO {
    private String id;
    private BigDecimal basePrice;
    private LocalDateTime biddingStartTime;
    private LocalDateTime biddingEndTime;


    public ProductDTO(Product p) {
        this.id = p.getId();
        this.basePrice = p.getBasePrice();
        this.biddingStartTime = p.getBiddingStartTime();
        this.biddingEndTime = p.getBiddingEndTime();
    }

    // Constructors, getters, and setters
}
