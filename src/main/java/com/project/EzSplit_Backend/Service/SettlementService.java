package com.project.EzSplit_Backend.Service;

import com.project.EzSplit_Backend.Dto.PaymentViewDto;
import com.project.EzSplit_Backend.Dto.SettlementListDto;
import com.project.EzSplit_Backend.Dto.SettlementTransactionDto;
import com.project.EzSplit_Backend.Entity.*;
import com.project.EzSplit_Backend.Entity.Type.PaymentStatus;
import com.project.EzSplit_Backend.Repository.*;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
@Service
@AllArgsConstructor
public class SettlementService {
    private final ExpenseRepository expenseRepository;
    private final ExpenseSplitRepository expenseSplitRepository;
    private final GroupRepository groupRepository;
    private final PaymentRepository paymentRepository;
    private final UserRepository userRepository;
    private final SettlementRepository settlementRepository;
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
    @Transactional
    public List<SettlementTransactionDto> generateSettlements(long groupId) {

        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group not found"));
        List<Expense> grpExpenses =
                expenseRepository.findByGroupIdAndStatus(groupId,PaymentStatus.PENDING);
        if(grpExpenses.isEmpty())
            return Collections.emptyList();
        System.out.println("Expenses size: " + grpExpenses.size());


        List<ExpenseSplit> grpSplits =
                expenseSplitRepository.findByExpenseIn(grpExpenses);

        System.out.println("Splits size: " + grpSplits.size());

        Settlement settlement = Settlement.builder()
                .group(group)
                .createdAt(LocalDateTime.now())
                .status(PaymentStatus.PENDING)
                .build();

        settlementRepository.save(settlement);



        Map<Long, Double> balances =
                calculateNetBalances(grpExpenses, grpSplits);

        System.out.println("Balances: " + balances);

        removeSettled(balances);
        System.out.println("Balances after cleanup: " + balances);
        List<SettlementTransactionDto> transactions =
                settleDebts(balances);

        // CREATE PAYMENTS HERE
        createPayments(settlement, transactions);

        for(Expense e: grpExpenses){
            e.setStatus(PaymentStatus.SETTLED);
        }
        expenseRepository.saveAll(grpExpenses);
        return transactions;
    }

    @Transactional
    public void createPayments(
            Settlement settlement,
            List<SettlementTransactionDto> transactions
    ) {

        List<Payment> payments = new ArrayList<>();

        for (SettlementTransactionDto txn : transactions) {

            User payer = userRepository.findById(txn.getFromUserId())
                    .orElseThrow();

            User receiver = userRepository.findById(txn.getToUserId())
                    .orElseThrow();

            Payment payment = Payment.builder()
                    .payer(payer)
                    .receiver(receiver)
                    .settlement(settlement)
                    .amount(txn.getAmount())
                    .status(PaymentStatus.PENDING)
                    .createdAt(LocalDateTime.now())
                    .paidAt(null)
                    .build();

            payments.add(payment);
        }

        paymentRepository.saveAll(payments);
    }
    public List<SettlementListDto> getGroupSettlements(Long groupId){

        List<Settlement> settlements =
                settlementRepository.findByGroup_IdOrderByCreatedAtDesc(groupId);
        System.out.println("Settlements size: " + settlements.size());
        return settlements.stream()
                .map(s -> new SettlementListDto(
                        s.getId(),
                        s.getCreatedAt(),
                        s.getStatus()
                ))
                .toList();
    }



}