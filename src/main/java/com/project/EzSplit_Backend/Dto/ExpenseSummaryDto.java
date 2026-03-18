package com.project.EzSplit_Backend.Dto;

import lombok.Data;

@Data
public class ExpenseSummaryDto {

    private Long expenseId;
    private String title;
    private Double amount;
    private String paidBy;
    private boolean paidByYou;
}