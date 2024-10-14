package com.work.scheduler.client;
import com.work.scheduler.dto.ProductDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
public class ProductClient {

    @Autowired
    private RestTemplate restTemplate;

    public List<ProductDTO> getExpiredProducts() {
        ResponseEntity<List<ProductDTO>> response = restTemplate.exchange(
                "http://localhost:8082/v1/auction/product/expired",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<ProductDTO>>() {});
        return response.getBody();
    }
}
