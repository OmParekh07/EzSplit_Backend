package com.project.EzSplit_Backend.Repository;

import com.project.EzSplit_Backend.Entity.Expense;
import com.project.EzSplit_Backend.Entity.ExpenseSplit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExpenseSplitRepository extends JpaRepository<ExpenseSplit, Long> {

    List<ExpenseSplit> findByExpense(Expense expense);

}