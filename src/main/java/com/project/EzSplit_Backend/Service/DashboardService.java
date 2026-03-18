package com.project.EzSplit_Backend.Service;

import com.project.EzSplit_Backend.Dto.DashboardDto;
import com.project.EzSplit_Backend.Dto.ExpenseSummaryDto;
import com.project.EzSplit_Backend.Dto.UpdateDetailDto;
import com.project.EzSplit_Backend.Entity.Expense;
import com.project.EzSplit_Backend.Entity.User;
import com.project.EzSplit_Backend.Repository.*;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class DashboardService {

    private final UserRepository userRepository;
    private final GroupRepository groupRepository;
    private final PaymentRepository paymentRepository;
    private final ExpenseRepository expenseRepository;

    public DashboardDto getDashboard(Long userId) {

        DashboardDto dto = new DashboardDto();

        // 1️⃣ Load user info
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        dto.setName(user.getName());
        dto.setUpiId(user.getUpiID());

        // 2️⃣ Total you owe
        Double owe = paymentRepository.sumYouOwe(userId);
        dto.setTotalYouOwe(owe == null ? 0 : owe);

        // 3️⃣ Total you paid
        Double paid = paymentRepository.sumOwedToYou(userId);
        dto.setTotalYouPaid(paid == null ? 0 : paid);

        // 4️⃣ Total groups
        int groups = groupRepository.countGroupsByUser(userId);
        dto.setTotalGroups(groups);



        List<Expense> expenses =
                expenseRepository.findRecentExpenses(userId);

        List<ExpenseSummaryDto> expenseDtos =
                expenses.stream().map(e -> {

                    ExpenseSummaryDto exp = new ExpenseSummaryDto();

                    exp.setExpenseId(e.getId());
                    exp.setTitle(e.getDescription());
                    exp.setAmount(e.getAmount());
                    exp.setPaidBy(e.getPaidBy().getName());

                    exp.setPaidByYou(
                            e.getPaidBy().getId().equals(userId)
                    );

                    return exp;

                }).toList();

        dto.setRecentExpenses(expenseDtos);

        return dto;
    }

    @Transactional
    public String updateDashboard(Long id, UpdateDetailDto dto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setName(dto.getName());
        user.setUpiID(dto.getUpiId());
        userRepository.save(user);
        return "UPDATED";
    }
}