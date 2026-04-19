package com.adil.ExpenseTracker.exception;

import java.time.LocalDate;

public class DailyLimitExceededException extends RuntimeException {

    public DailyLimitExceededException(LocalDate date) {
        super("Daily limit of 10 expenses reached for date: " + date);
    }
}
