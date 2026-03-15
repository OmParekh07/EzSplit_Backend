package com.project.EzSplit_Backend.Dto;
import com.project.EzSplit_Backend.Entity.Type.PaymentStatus;
import com.project.EzSplit_Backend.Entity.Type.SplitType;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ExpenseResponseDto {

    private Long id;

    private String description;

    private Double amount;

    private String paidBy;
    private PaymentStatus status;
    private SplitType splitType;
    private LocalDateTime createdAt;
}