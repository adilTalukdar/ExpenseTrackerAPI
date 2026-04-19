package com.adil.ExpenseTracker.dto;

import com.adil.ExpenseTracker.enums.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExpenseResponse {

    private Long id;
    private String title;
    private Double amount;
    private Category category;
    private LocalDate date;
    private LocalDateTime createdAt;
}
