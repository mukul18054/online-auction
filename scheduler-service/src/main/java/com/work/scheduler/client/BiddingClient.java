package com.work.scheduler.client;

import com.work.scheduler.dto.BidResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

@Component
public class BiddingClient {

    @Autowired
    private RestTemplate restTemplate;

    // Endpoint for fetching bids for a specific product
    public BidResponse getBidsForProduct(String productId) {
        String url = "http://localhost:8081/v1/auction/bidding/getBidsByProductId/" + productId;

        try {
            ResponseEntity<BidResponse> response = restTemplate.getForEntity(url, BidResponse.class);

            // Check for NOT_FOUND status and handle it
            if (response.getStatusCode() == HttpStatus.NOT_FOUND) {
                // Handle the not found case, return an empty response or take other actions
                return new BidResponse(Collections.emptyList()); // Return an empty list
            }

            // Return the body if found
            return response.getBody();

        } catch (HttpClientErrorException.NotFound e) {
            // Handle exception when 404 NOT_FOUND is returned
            return new BidResponse(Collections.emptyList()); // Return an empty list on exception
        } catch (RestClientException e) {
            // Handle other RestTemplate exceptions (timeouts, connectivity issues, etc.)
            throw new RuntimeException("Failed to retrieve bids for product ID: " + productId, e);
        } catch (Exception e) {
            // Handle other exceptions
            throw new RuntimeException("An error occurred while fetching bids for product ID: " + productId, e);
        }
    }

}
