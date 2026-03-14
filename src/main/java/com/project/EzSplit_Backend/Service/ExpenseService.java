package com.project.EzSplit_Backend.Service;

import com.project.EzSplit_Backend.Dto.CreateExpenseRequestDto;
import com.project.EzSplit_Backend.Dto.ExpenseResponseDto;
import com.project.EzSplit_Backend.Dto.SplitDetailDto;
import com.project.EzSplit_Backend.Entity.*;
import com.project.EzSplit_Backend.Entity.Type.SplitType;
import com.project.EzSplit_Backend.Repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final ExpenseSplitRepository expenseSplitRepository;
    private final GroupRepository groupRepository;
    private final UserRepository userRepository;

    public String createExpense(CreateExpenseRequestDto request, Long paidByUserId){

        // 1️⃣ Find group
        Group group = groupRepository.findById(request.getGroupId())
                .orElseThrow(() -> new RuntimeException("Group not found"));

        // 2️⃣ Find payer
        User payer = userRepository.findById(paidByUserId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 3️⃣ Create Expense
        Expense expense = expenseRepository.save(
                Expense.builder()
                        .description(request.getDescription())
                        .amount(request.getAmount())
                        .group(group)
                        .paidBy(payer)
                        .build()
        );

        // 4️⃣ Handle Split Logic
        switch (request.getSplitType()) {

            case EQUAL -> handleEqualSplit(request, expense,payer);

            case CUSTOM -> handleCustomSplit(request, expense);

            case PERCENTAGE -> handlePercentageSplit(request, expense);
        }

        return "Expense created successfully";
    }


    private void handleEqualSplit(CreateExpenseRequestDto request,
                                  Expense expense,
                                  User payer){

        List<String> emails = request.getSplits()
                .stream()
                .map(SplitDetailDto::getEmail)
                .toList();

        int participantCount = emails.contains(payer.getUsername())
                ? emails.size()
                : emails.size() + 1;

        double splitAmount = request.getAmount() / participantCount;

        // save splits for request members
        for (SplitDetailDto split : request.getSplits()) {

            User user = userRepository.findByUsername(split.getEmail())
                    .orElseThrow();

            expenseSplitRepository.save(
                    ExpenseSplit.builder()
                            .expense(expense)
                            .user(user)
                            .amount(splitAmount)
                            .build()
            );
        }

        // add payer if not included
        if(!emails.contains(payer.getUsername())){

            expenseSplitRepository.save(
                    ExpenseSplit.builder()
                            .expense(expense)
                            .user(payer)
                            .amount(splitAmount)
                            .build()
            );
        }
    }


    private void handleCustomSplit(CreateExpenseRequestDto request, Expense expense){

        double total = 0;

        for (SplitDetailDto split : request.getSplits()) {
            total += split.getValue();
        }

        if(total != request.getAmount())
            throw new RuntimeException("Custom split total does not match expense amount");

        for (SplitDetailDto split : request.getSplits()) {

            User user = userRepository.findByUsername(split.getEmail())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            expenseSplitRepository.save(
                    ExpenseSplit.builder()
                            .expense(expense)
                            .user(user)
                            .amount(split.getValue())
                            .build()
            );
        }
    }


    private void handlePercentageSplit(CreateExpenseRequestDto request, Expense expense){

        double percentageTotal = 0;

        for (SplitDetailDto split : request.getSplits()) {
            percentageTotal += split.getValue();
        }

        if(percentageTotal != 100)
            throw new RuntimeException("Total percentage must be 100");

        for (SplitDetailDto split : request.getSplits()) {

            double amount = request.getAmount() * split.getValue() / 100;

            User user = userRepository.findByUsername(split.getEmail())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            expenseSplitRepository.save(
                    ExpenseSplit.builder()
                            .expense(expense)
                            .user(user)
                            .amount(amount)
                            .build()
            );
        }
    }

    public List<ExpenseResponseDto> getGroupExpenses(Long groupId){

        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group not found"));

        List<Expense> expenses = expenseRepository.findByGroup(group);

        return expenses.stream()
                .map(expense -> ExpenseResponseDto.builder()
                        .id(expense.getId())
                        .description(expense.getDescription())
                        .amount(expense.getAmount())
                        .paidBy(expense.getPaidBy().getUsername())
                        .createdAt(expense.getCreatedAt())
                        .build())
                .toList();
    }
}