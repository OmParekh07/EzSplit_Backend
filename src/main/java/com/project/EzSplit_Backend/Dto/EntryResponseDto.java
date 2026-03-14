package com.project.EzSplit_Backend.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EntryResponseDto {
    private Long id;

    private String description;

    private Double amount;

    private Long paidBy;

    private String splitType;

    private Map<Long, Double> splits;

    private String mode;

    private String date;
}
