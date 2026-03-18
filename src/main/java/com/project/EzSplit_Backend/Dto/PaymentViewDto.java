package com.project.EzSplit_Backend.Dto;

import com.project.EzSplit_Backend.Entity.Type.PaymentStatus;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentViewDto {

    private Long id;
    private String payerName;
    private String receiverName;
    private Long payerId;
    private Long receiverId;
    private Double amount;
    private PaymentStatus status;
    private String upiLink;
    private boolean canPay;
    private boolean canConfirm;

}