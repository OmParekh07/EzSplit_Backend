package com.project.EzSplit_Backend.Repository;

import com.project.EzSplit_Backend.Entity.Expense;
import com.project.EzSplit_Backend.Entity.Group;
import com.project.EzSplit_Backend.Entity.Type.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {

    List<Expense> findByGroup(Group group);
    List<Expense> findByGroupIdAndStatus(Long groupId, PaymentStatus status);
    int countByGroup(Group group);

    @Query("""
       SELECT SUM(e.amount)
       FROM Expense e
       WHERE e.group = :group
       """)
    Double sumAmountByGroup(Group group);

    List<Expense> findByGroupAndStatus(Group group, PaymentStatus pending);
}