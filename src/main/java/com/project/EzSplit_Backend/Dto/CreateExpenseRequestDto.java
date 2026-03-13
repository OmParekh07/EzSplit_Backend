package com.project.EzSplit_Backend.Dto;

import com.project.EzSplit_Backend.Entity.Type.SplitType;
import lombok.Data;

import java.util.List;

@Data
public class CreateExpenseRequestDto {

    private Long groupId;

    private String description;

    private Double amount;

    private SplitType splitType;

    private List<SplitDetailDto> splits;

}