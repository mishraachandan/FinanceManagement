package com.project.financemanagement.service.income;

import com.project.financemanagement.entity.Income;

import java.util.List;

public interface IncomeService {
    Income addIncome(Income income);
    Income updateIncome(Income income);
    void deleteIncome(Long incomeId);
    List<Income> getAllIncomesByUserId(Long userId);
}