package com.project.financemanagement.service.expense;

import com.project.financemanagement.entity.Expense;

import java.util.List;

public interface ExpenseService {
    Expense addExpense(Expense expense);
    Expense updateExpense(Expense expense);
    void deleteExpense(Long expenseId);
    List<Expense> getAllExpensesByUserId(Long userId);
}