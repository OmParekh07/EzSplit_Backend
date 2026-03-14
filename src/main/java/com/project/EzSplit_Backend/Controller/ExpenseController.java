package com.project.EzSplit_Backend.Controller;

import com.project.EzSplit_Backend.Dto.CreateExpenseRequestDto;
import com.project.EzSplit_Backend.Dto.ExpenseResponseDto;
import com.project.EzSplit_Backend.Entity.User;
import com.project.EzSplit_Backend.Service.ExpenseService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ExpenseController {
    private final ExpenseService expenseService;
    @PostMapping("/expenses")
    public String createExpense(@RequestBody CreateExpenseRequestDto request,
                                Authentication authentication) {

        User user = (User) authentication.getPrincipal();

        return expenseService.createExpense(request, user.getId());
    }

    @GetMapping("/{groupId}/expenses")
    public List<ExpenseResponseDto> getGroupExpenses(@PathVariable Long groupId){

        return expenseService.getGroupExpenses(groupId);
    }

}