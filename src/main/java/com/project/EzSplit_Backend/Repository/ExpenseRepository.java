package com.project.EzSplit_Backend.Repository;

import com.project.EzSplit_Backend.Entity.Expense;
import com.project.EzSplit_Backend.Entity.Group;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {

    List<Expense> findByGroup(Group group);

}