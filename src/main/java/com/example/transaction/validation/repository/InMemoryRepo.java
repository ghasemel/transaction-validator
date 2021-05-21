package com.example.transaction.validation.repository;

import com.example.transaction.validation.domain.Transaction;

import java.util.concurrent.ConcurrentHashMap;

public class InMemoryRepo implements Repo {
    private final ConcurrentHashMap<String, String> memory;

    public InMemoryRepo() {
        memory = new ConcurrentHashMap<>();
    }

    @Override
    public void add(Transaction transaction) {
        memory.put(transaction.getReference(), transaction.getDescription());
    }

    @Override
    public boolean exists(Transaction transaction) {
        return memory.containsKey(transaction.getReference());
    }
}
