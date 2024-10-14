
package com.work.product.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class CreateProductDTO {
    private String name;
    private String description;
    private BigDecimal basePrice;
    private String categoryId;
    private LocalDateTime biddingStartTime;
    private LocalDateTime biddingEndTime;

    // Constructors, getters, and setters
}