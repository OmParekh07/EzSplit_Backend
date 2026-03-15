package com.project.EzSplit_Backend.Dto;

import com.project.EzSplit_Backend.Entity.Type.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
public class SettlementListDto {

    private Long settlementId;
    private LocalDateTime createdAt;
    private PaymentStatus status;
}