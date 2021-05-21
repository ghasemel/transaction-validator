package com.example.transaction.validation.repository;

import com.example.transaction.validation.domain.Transaction;

public interface Repo {

    void add(Transaction transaction);
    boolean exists(Transaction transaction);
}
