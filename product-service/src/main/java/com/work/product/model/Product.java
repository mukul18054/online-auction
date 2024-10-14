package com.work.product.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.*;

//@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "products")
public class Product {
    public enum ProductStatus {
        ACTIVE, SOLD, DEACTIVATED, DRAFT, EXPIRED, PAYMENT_PENDING
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;
    private String name;
    private String description;
    private BigDecimal basePrice;
    private String categoryId;
    private LocalDateTime biddingStartTime;
    private LocalDateTime biddingEndTime;
    private ProductStatus status;
    // User who created the product (relationship with User model)
    @JsonIgnore
    private String createdBy; // Added createdBy field

    // Product Images
    private List<String> images = new ArrayList<>(); // Added images field

}
