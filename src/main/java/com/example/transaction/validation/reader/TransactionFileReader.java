package com.example.transaction.validation.reader;

import com.example.transaction.validation.domain.Transaction;

import java.io.Closeable;

public interface TransactionFileReader extends Closeable {
    boolean hasNext();
    Transaction readNext();
}
