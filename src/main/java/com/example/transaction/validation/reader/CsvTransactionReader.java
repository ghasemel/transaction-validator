package com.example.transaction.validation.reader;

import com.example.transaction.validation.domain.Transaction;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import lombok.SneakyThrows;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Iterator;

public class CsvTransactionReader implements TransactionFileReader {

    private final Iterator<Transaction> iterator;
    private final Reader reader;

    public CsvTransactionReader(Path filePath) throws IOException {
        reader = Files.newBufferedReader(filePath);
        CsvToBean<Transaction> csvReader = new CsvToBeanBuilder<Transaction>(reader).withType(Transaction.class).build();
        iterator = csvReader.iterator();
    }

    @SneakyThrows
    @Override
    public boolean hasNext() {
        return iterator.hasNext();
    }

    @Override
    public Transaction readNext() {
        return iterator.next();
    }

    @Override
    public void close() throws IOException {
        if (reader != null)
            reader.close();
    }
}
