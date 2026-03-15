package com.project.EzSplit_Backend.Dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class GroupSummaryDto {

    private Long groupId;
    private String name;
    private String description;
    private int membersCount;
    private int totalExpenses;
    private Double totalSpent;

}