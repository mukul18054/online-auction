package com.work.product.service;

import com.work.product.dto.MessageDTO;
import com.work.product.dto.ProductDTO;
import com.work.product.model.Product;
import com.work.product.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ProductService {


    public Product createProduct(Product product);
    public void updateProduct(Product product);
    public Optional<Product> getProduct(String id);

    public List<Product> getAllProducts();

    public List<Product> getProductsByCategory(String categoryId);

    public List<Product> getActiveAuctions();

    public boolean isValidBid(String productId, BigDecimal bidAmount);

    public List<ProductDTO> findActiveProductsBeforeNow();


}