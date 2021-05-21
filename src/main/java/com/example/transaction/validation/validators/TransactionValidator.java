package com.example.transaction.validation.validators;

import com.example.transaction.validation.domain.Transaction;

public interface TransactionValidator {
    boolean isUnique(Transaction transaction);
    boolean ValidateBalance(Transaction transaction);
}
