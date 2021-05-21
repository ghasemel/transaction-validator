package com.example.transaction.validation.convertor;

import com.example.transaction.validation.domain.ReportEntry;
import com.example.transaction.validation.domain.Transaction;

import java.util.Optional;

public class TransactionToReportEntryConvertor implements Convertor<Transaction, ReportEntry> {

    @Override
    public Optional<ReportEntry> convert(Transaction transaction) {
        if (transaction == null)
            return Optional.empty();

        return Optional.of(ReportEntry.builder()
                .reference(transaction.getReference())
                .description(transaction.getDescription())
                .build());
    }
}
