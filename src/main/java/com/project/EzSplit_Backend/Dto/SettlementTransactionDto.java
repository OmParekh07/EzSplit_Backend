package com.project.EzSplit_Backend.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SettlementTransactionDto {
    private Long fromUserId;
    private Long toUserId;
    private Double amount;
}
