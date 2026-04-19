package com.adil.ExpenseTracker.service;

import com.adil.ExpenseTracker.dto.ExpenseRequest;
import com.adil.ExpenseTracker.dto.ExpenseResponse;
import com.adil.ExpenseTracker.dto.TotalResponse;
import com.adil.ExpenseTracker.enums.Category;
import com.adil.ExpenseTracker.exception.DailyLimitExceededException;
import com.adil.ExpenseTracker.exception.DuplicateExpenseException;
import com.adil.ExpenseTracker.exception.ExpenseNotFoundException;
import com.adil.ExpenseTracker.model.Expense;
import com.adil.ExpenseTracker.repository.ExpenseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class ExpenseService {

    private static final int DAILY_LIMIT = 10;

    private final ExpenseRepository expenseRepository;

    @Transactional
    public ExpenseResponse createExpense(ExpenseRequest request) {
        if (expenseRepository.existsByTitleAndAmountAndDate(request.getTitle(), request.getAmount(), request.getDate())) {
            throw new DuplicateExpenseException();
        }
        long dailyCount = expenseRepository.countByDate(request.getDate());
        if (dailyCount >= DAILY_LIMIT) {
            throw new DailyLimitExceededException(request.getDate());
        }
        Expense expense = Expense.builder()
                .title(request.getTitle())
                .amount(request.getAmount())
                .category(request.getCategory())
                .date(request.getDate())
                .build();
        Expense saved = expenseRepository.save(expense);
        return toResponse(saved);
    }

    @Transactional(readOnly = true)
    public Page<ExpenseResponse> getAllExpenses(Category category, LocalDate startDate, LocalDate endDate, Pageable pageable) {
        return expenseRepository.findWithFilters(category, startDate, endDate, pageable)
                .map(this::toResponse);
    }

    @Transactional(readOnly = true)
    public ExpenseResponse getExpenseById(Long id) {
        Expense expense = expenseRepository.findById(id)
                .orElseThrow(() -> new ExpenseNotFoundException(id));
        return toResponse(expense);
    }

    @Transactional
    public ExpenseResponse updateExpense(Long id, ExpenseRequest request) {
        Expense existing = expenseRepository.findById(id)
                .orElseThrow(() -> new ExpenseNotFoundException(id));

        boolean titleChanged = !existing.getTitle().equals(request.getTitle());
        boolean amountChanged = !existing.getAmount().equals(request.getAmount());
        boolean dateChanged = !existing.getDate().equals(request.getDate());

        if (titleChanged || amountChanged || dateChanged) {
            if (expenseRepository.existsByTitleAndAmountAndDate(request.getTitle(), request.getAmount(), request.getDate())) {
                throw new DuplicateExpenseException();
            }
        }

        if (dateChanged && request.getDate() != null) {
            long dailyCount = expenseRepository.countByDate(request.getDate());
            if (dailyCount >= DAILY_LIMIT) {
                throw new DailyLimitExceededException(request.getDate());
            }
        }

        existing.setTitle(request.getTitle());
        existing.setAmount(request.getAmount());
        existing.setCategory(request.getCategory());
        existing.setDate(request.getDate());

        Expense updated = expenseRepository.save(existing);
        return toResponse(updated);
    }

    @Transactional
    public void deleteExpense(Long id) {
        if (!expenseRepository.existsById(id)) {
            throw new ExpenseNotFoundException(id);
        }
        expenseRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public TotalResponse getTotalSpending(Category category, LocalDate startDate, LocalDate endDate) {
        Double total = expenseRepository.sumAmountWithFilters(category, startDate, endDate);
        long count = expenseRepository.countWithFilters(category, startDate, endDate);
        return TotalResponse.builder()
                .total(total != null ? total : 0.0)
                .count(count)
                .build();
    }

    private ExpenseResponse toResponse(Expense expense) {
        return ExpenseResponse.builder()
                .id(expense.getId())
                .title(expense.getTitle())
                .amount(expense.getAmount())
                .category(expense.getCategory())
                .date(expense.getDate())
                .createdAt(expense.getCreatedAt())
                .build();
    }
}
