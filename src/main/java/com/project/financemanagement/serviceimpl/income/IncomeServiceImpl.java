package com.project.financemanagement.serviceimpl.income;

import com.project.financemanagement.entity.Income;
import com.project.financemanagement.repository.IncomeRepository;
import com.project.financemanagement.service.income.IncomeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IncomeServiceImpl implements IncomeService {

    @Autowired
    private IncomeRepository incomeRepository;

    @Override
    public Income addIncome(Income income) {
        return incomeRepository.save(income);
    }

    @Override
    public Income updateIncome(Income income) {
        return incomeRepository.save(income);
    }

    @Override
    public void deleteIncome(Long incomeId) {
        incomeRepository.deleteById(incomeId);
    }

    @Override
    public List<Income> getAllIncomesByUserId(Long userId) {
        return incomeRepository.findByUserId(userId);
    }
}