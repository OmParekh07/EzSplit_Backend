package com.project.EzSplit_Backend.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreatePaymentRequestDto {

    private Long payerId;
    private Long receiverId;
    private Double amount;
    private String receiverUpi;
    private String receiverName;

    // getters and setters
}
