package com.work.scheduler.service;

import com.work.scheduler.client.BiddingClient;
import com.work.scheduler.client.ProductClient;
import com.work.scheduler.dto.BidDTO;
import com.work.scheduler.dto.BidResponse;
import com.work.scheduler.dto.ProductDTO;
import com.work.scheduler.dto.WinnerDTO;
import com.work.scheduler.impl.SchedulerServiceImpl;
import com.work.scheduler.producer.KafkaProducer;
import com.work.scheduler.strategy.WinnerDeterminationStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class SchedulerServiceTest {
    private SchedulerServiceImpl schedulerService;

    @Mock
    private ProductClient productClient;

    @Mock
    private BiddingClient biddingClient;

    @Mock
    private KafkaProducer kafkaProducer;

    @Mock
    private WinnerDeterminationStrategy winnerDeterminationStrategy;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        schedulerService = new SchedulerServiceImpl(productClient, biddingClient, kafkaProducer, winnerDeterminationStrategy);
    }

    @Test
    void processExpiredProducts_shouldProcessAllExpiredProducts() {
        // Arrange
        ProductDTO product1 = new ProductDTO("1", BigDecimal.TEN, LocalDateTime.now().minusDays(1), LocalDateTime.now().minusHours(1));
        ProductDTO product2 = new ProductDTO("2", BigDecimal.valueOf(20), LocalDateTime.now().minusDays(1), LocalDateTime.now().minusHours(1));
        List<ProductDTO> expiredProducts = Arrays.asList(product1, product2);

        // Mock the productClient to return a list of expired products
        when(productClient.getExpiredProducts()).thenReturn(expiredProducts);

        // Mock the biddingClient to return BidResponse with an empty list of bids for each product
        when(biddingClient.getBidsForProduct("1")).thenReturn(new BidResponse(Collections.emptyList()));
        when(biddingClient.getBidsForProduct("2")).thenReturn(new BidResponse(Collections.emptyList()));

        // Act
        schedulerService.processExpiredProducts();

        // Assert
        verify(productClient, times(1)).getExpiredProducts();
        verify(biddingClient, times(1)).getBidsForProduct("1");
        verify(biddingClient, times(1)).getBidsForProduct("2");
    }


    @Test
    void processExpiredProduct_withWinner_shouldSendNotification() {
        // Arrange
        ProductDTO product = new ProductDTO("1", BigDecimal.TEN, LocalDateTime.now().minusDays(1), LocalDateTime.now().minusHours(1));
        BidDTO bid1 = new BidDTO("bid1", "1", "user1", 15.0, LocalDateTime.now());
        BidDTO bid2 = new BidDTO("bid2", "1", "user2", 20.0, LocalDateTime.now());
        List<BidDTO> bids = Arrays.asList(bid1, bid2);

        when(biddingClient.getBidsForProduct("1")).thenReturn(new BidResponse(bids));

        WinnerDTO expectedWinner = new WinnerDTO("user2", "1", 20.0);
        when(winnerDeterminationStrategy.determineWinner(product, bids)).thenReturn(expectedWinner);

        // Act
        schedulerService.processExpiredProduct(product);

        // Assert
        verify(biddingClient, times(1)).getBidsForProduct("1");
        verify(winnerDeterminationStrategy, times(1)).determineWinner(product, bids);
        verify(kafkaProducer, times(1)).sendWinnerNotification(expectedWinner);
    }


    @Test
    void processExpiredProduct_withoutWinner_shouldNotSendNotification() {
        // Arrange
        ProductDTO product = new ProductDTO("1", BigDecimal.TEN, LocalDateTime.now().minusDays(1), LocalDateTime.now().minusHours(1));
        List<BidDTO> bids = Collections.emptyList();

        when(biddingClient.getBidsForProduct("1")).thenReturn(new BidResponse(bids));
        when(winnerDeterminationStrategy.determineWinner(product, bids)).thenReturn(null);

        // Act
        schedulerService.processExpiredProduct(product);

        // Assert
        verify(biddingClient, times(1)).getBidsForProduct("1");
        verify(winnerDeterminationStrategy, times(1)).determineWinner(product, bids);
        verify(kafkaProducer, never()).sendWinnerNotification(any());
    }

    @Test
    void testProcessExpiredProducts_NoExpiredProducts() {
        // Arrange
        when(productClient.getExpiredProducts()).thenReturn(Collections.emptyList());

        // Act
        schedulerService.processExpiredProducts();

        // Assert
        verify(productClient, times(1)).getExpiredProducts();
        verify(biddingClient, never()).getBidsForProduct(anyString());
        verify(kafkaProducer, never()).sendWinnerNotification(any());
    }

    @Test
    void testProcessExpiredProducts_NoBids() {
        // Arrange
        ProductDTO product = new ProductDTO("product1", BigDecimal.TEN, LocalDateTime.now().minusDays(1), LocalDateTime.now().minusHours(1));

        when(productClient.getExpiredProducts()).thenReturn(Arrays.asList(product));
        when(biddingClient.getBidsForProduct(product.getId())).thenReturn(new BidResponse(Collections.emptyList()));

        // Act
        schedulerService.processExpiredProducts();

        // Assert
        verify(biddingClient, times(1)).getBidsForProduct(product.getId());
        verify(productClient, times(1)).getExpiredProducts();
        verify(kafkaProducer, never()).sendWinnerNotification(any());
    }

    @Test
    void testProcessExpiredProducts_WinnerDetermined() {
        ProductDTO product = new ProductDTO();
        product.setId("product1");
        LocalDateTime biddingEndTime = LocalDateTime.now().minusDays(1);
        BidDTO bid1 = new BidDTO("bid1", "product1", "user1", 500.0, biddingEndTime);
        BidDTO bid2 = new BidDTO("bid2", "product1", "user2", 600.0, biddingEndTime);

        WinnerDTO capturedWinner = new WinnerDTO("user2", "product1", 600.0);

        when(productClient.getExpiredProducts()).thenReturn(Arrays.asList(product));
        when(biddingClient.getBidsForProduct(product.getId())).thenReturn(new BidResponse(Arrays.asList(bid1, bid2)));
        when(winnerDeterminationStrategy.determineWinner(product, Arrays.asList(bid1, bid2)))
                .thenReturn(capturedWinner);

        schedulerService.processExpiredProducts();

        verify(kafkaProducer).sendWinnerNotification(capturedWinner);

        assertEquals("user2", capturedWinner.getEmailId());
        assertEquals("product1", capturedWinner.getProductId());
        assertEquals(600.0, capturedWinner.getWinningBidAmount());


        verify(biddingClient).getBidsForProduct(product.getId());
        verify(productClient).getExpiredProducts();
    }

    /*
    @Test
    void processExpiredProduct_withoutWinner_shouldNotSendNotification() {
        // Arrange
        ProductDTO product = new ProductDTO("1", "Product 1", BigDecimal.TEN);
        List<BidDTO> bids = Collections.emptyList();

        when(biddingClient.getBidsForProduct("1")).thenReturn(new BiddingClient.BidResponse(bids));
        when(winnerDeterminationStrategy.determineWinner(product, bids)).thenReturn(null);

        // Act
        schedulerService.processExpiredProduct(product);

        // Assert
        verify(biddingClient, times(1)).getBidsForProduct("1");
        verify(winnerDeterminationStrategy, times(1)).determineWinner(product, bids);
        verify(kafkaProducer, never()).sendWinnerNotification(any());
    }


    @Test
    void testProcessExpiredProducts_NoExpiredProducts() {
        when(productClient.getExpiredProducts()).thenReturn(Collections.emptyList());

        schedulerService.processExpiredProducts();

        verify(biddingClient, never()).getBidsForProduct(anyString());
        verify(kafkaProducer, never()).sendWinnerNotification(any());
    }

    @Test
    void testProcessExpiredProducts_NoBids() {
        ProductDTO product = new ProductDTO();
        product.setId("product1");

        when(productClient.getExpiredProducts()).thenReturn(Arrays.asList(product));
        when(biddingClient.getBidsForProduct(product.getId())).thenReturn(new BidResponse(Collections.emptyList()));

        schedulerService.processExpiredProducts();

        verify(biddingClient).getBidsForProduct(product.getId());
        verify(productClient).getExpiredProducts();
        verify(kafkaProducer, never()).sendWinnerNotification(any());
    }

     */






}



