package com.work.scheduler.dto;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class WinnerDTO {

    private String emailId;
    private String productId;
    private Double winningBidAmount;


}
