package com.project.EzSplit_Backend.Service;

import com.project.EzSplit_Backend.Dto.SettlementTransactionDto;
import com.project.EzSplit_Backend.Entity.Expense;
import com.project.EzSplit_Backend.Entity.ExpenseSplit;

import java.util.*;

public class SettlementService {

    // STEP 1: Calculate Net Balances
    public Map<Long, Double> calculateNetBalances(
            List<Expense> expenses,
            List<ExpenseSplit> splits
    ) {

        Map<Long, Double> balance = new HashMap<>();

        // Add credits for payer
        for (Expense expense : expenses) {

            Long payerId = expense.getPaidBy().getId();

            balance.merge(
                    payerId,
                    expense.getAmount(),
                    Double::sum
            );
        }

        // Subtract shares
        for (ExpenseSplit split : splits) {

            Long userId = split.getUser().getId();

            balance.merge(
                    userId,
                    -split.getAmount(),
                    Double::sum
            );
        }

        return balance;
    }

    // STEP 2: Remove settled users
    private void removeSettled(Map<Long, Double> balance) {

        balance.entrySet().removeIf(
                e -> Math.abs(e.getValue()) < 0.01
        );
    }

    // STEP 3: Debt Settlement Algorithm
    public List<SettlementTransactionDto> settleDebts(
            Map<Long, Double> balance
    ) {

        List<SettlementTransactionDto> transactions = new ArrayList<>();

        PriorityQueue<long[]> creditors =
                new PriorityQueue<>((a, b) -> Double.compare(b[1], a[1]));

        PriorityQueue<long[]> debtors =
                new PriorityQueue<>((a, b) -> Double.compare(b[1], a[1]));

        for (Map.Entry<Long, Double> entry : balance.entrySet()) {

            Long userId = entry.getKey();
            Double amount = entry.getValue();

            if (amount > 0) {

                creditors.offer(new long[]{userId, Math.round(amount * 100)});

            } else if (amount < 0) {

                debtors.offer(new long[]{userId, Math.round(-amount * 100)});
            }
        }

        while (!creditors.isEmpty() && !debtors.isEmpty()) {

            long[] creditor = creditors.poll();
            long[] debtor = debtors.poll();

            Long creditorId = creditor[0];
            Long debtorId = debtor[0];

            double creditAmount = creditor[1] / 100.0;
            double debtAmount = debtor[1] / 100.0;

            double payment = Math.min(creditAmount, debtAmount);

            transactions.add(
                    SettlementTransactionDto.builder()
                            .fromUserId(debtorId)
                            .toUserId(creditorId)
                            .amount(payment)
                            .build()
            );

            double remainingCredit = creditAmount - payment;
            double remainingDebt = debtAmount - payment;

            if (remainingCredit > 0.01) {

                creditors.offer(
                        new long[]{
                                creditorId,
                                Math.round(remainingCredit * 100)
                        }
                );
            }

            if (remainingDebt > 0.01) {

                debtors.offer(
                        new long[]{
                                debtorId,
                                Math.round(remainingDebt * 100)
                        }
                );
            }
        }

        return transactions;
    }

    // MAIN METHOD USED BY CONTROLLER
    public List<SettlementTransactionDto> generateSettlements(
            List<Expense> expenses,
            List<ExpenseSplit> splits
    ) {

        Map<Long, Double> balances =
                calculateNetBalances(expenses, splits);

        removeSettled(balances);

        return settleDebts(balances);
    }

}