
package com.work.scheduler.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;


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


    // Constructors, getters, and setters
}
