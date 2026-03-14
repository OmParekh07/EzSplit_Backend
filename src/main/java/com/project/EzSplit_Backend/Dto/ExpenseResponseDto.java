package com.project.EzSplit_Backend.Dto;
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

    private LocalDateTime createdAt;
}