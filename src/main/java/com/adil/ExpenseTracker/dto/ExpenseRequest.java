package com.adil.ExpenseTracker.dto;

import com.adil.ExpenseTracker.enums.Category;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ExpenseRequest {

    @NotBlank(message = "Title must not be blank")
    @Size(min = 3, message = "Title must be at least 3 characters")
    private String title;

    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be greater than 0")
    private Double amount;

    @NotNull(message = "Category is required")
    private Category category;

    @NotNull(message = "Date is required")
    private LocalDate date;
}
