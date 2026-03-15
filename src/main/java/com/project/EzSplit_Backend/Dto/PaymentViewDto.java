package com.project.EzSplit_Backend.Dto;

import com.project.EzSplit_Backend.Entity.Type.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PaymentViewDto {
    private Long id;
    private String payerName;
    private String receiverName;
    private Double amount;
    private PaymentStatus status;
}