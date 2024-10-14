package com.work.notification.dto;


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
