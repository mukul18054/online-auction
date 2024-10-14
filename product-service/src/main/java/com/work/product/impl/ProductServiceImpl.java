package com.work.product.impl;

import com.work.product.dto.ProductDTO;
import com.work.product.model.Product;
import com.work.product.repository.ProductRepository;
import com.work.product.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Product createProduct(Product product) {
        return productRepository.save(product);
    }
    public void updateProduct(Product product) {
        productRepository.save(product);
    }
    public Optional<Product> getProduct(String id) {
        return productRepository.findById(id);
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public List<Product> getProductsByCategory(String categoryId) {
        return productRepository.findByCategoryId(categoryId);
    }

    public List<Product> getActiveAuctions() {
        LocalDateTime now = LocalDateTime.now();
        return productRepository.findByBiddingStartTimeBeforeAndBiddingEndTimeAfter(now, now);
    }

    public boolean isValidBid(String productId, BigDecimal bidAmount) {
        Optional<Product> product = productRepository.findById(productId);
        return product.map(p -> bidAmount.compareTo(p.getBasePrice()) >= 0).orElse(false);
    }

    public List<ProductDTO> findActiveProductsBeforeNow() {
        LocalDateTime now = LocalDateTime.now();
        // find all products with bidding end time before now and its STATUS is ACTIVE
        List<Product> expiredProducts = productRepository.findProductsBeforeNowAndActive(now);
        // iterate over expiredProducts and change status to EXPIRED
        expiredProducts.forEach(p -> {
            p.setStatus(Product.ProductStatus.EXPIRED);
            productRepository.save(p);
        });
        List<ProductDTO> expiredProductsDto = new ArrayList<>();
        expiredProducts.forEach(product -> expiredProductsDto.add(new ProductDTO(product)));
        return expiredProductsDto;
    }
}
