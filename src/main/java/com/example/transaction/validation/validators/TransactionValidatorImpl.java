package com.example.transaction.validation.validators;

import com.example.transaction.validation.domain.Transaction;
import com.example.transaction.validation.repository.Repo;

public class TransactionValidatorImpl implements TransactionValidator {

    private final Repo repo;

    public TransactionValidatorImpl(Repo repo) {
        this.repo = repo;
    }

    @Override
    public boolean isUnique(Transaction transaction) {
        return !repo.exists(transaction);
    }

    @Override
    public boolean ValidateBalance(Transaction transaction) {
        return transaction.getEndBalance().equals(transaction.getStartBalance().add(transaction.getMutation()));
    }
}
