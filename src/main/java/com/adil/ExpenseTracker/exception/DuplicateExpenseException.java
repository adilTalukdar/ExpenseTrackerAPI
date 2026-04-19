package com.adil.ExpenseTracker.exception;

public class DuplicateExpenseException extends RuntimeException {

    public DuplicateExpenseException() {
        super("An expense with the same title, amount, and date already exists");
    }
}
