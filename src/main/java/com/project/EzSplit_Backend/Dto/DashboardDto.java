package com.project.EzSplit_Backend.Dto;

import lombok.Data;
import java.util.List;

@Data
public class DashboardDto {

    private String name;
    private String upiId;

    private double totalYouOwe;
    private double totalYouPaid;

    private int totalGroups;

    private List<ExpenseSummaryDto> recentExpenses;
}