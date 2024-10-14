package com.work.scheduler.service;

import com.work.scheduler.dto.ProductDTO;

public interface SchedulerService {
    void processExpiredProducts();
    void processExpiredProduct(ProductDTO product);
}