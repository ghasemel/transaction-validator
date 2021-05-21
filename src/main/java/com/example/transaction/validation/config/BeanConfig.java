package com.example.transaction.validation.config;

import com.example.transaction.validation.convertor.TransactionToReportEntryConvertor;
import com.example.transaction.validation.repository.InMemoryRepo;
import com.example.transaction.validation.repository.Repo;
import com.example.transaction.validation.validators.TransactionValidator;
import com.example.transaction.validation.validators.TransactionValidatorImpl;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
public class BeanConfig {

    @Getter
    private static TransactionValidator transactionValidator;

    @Getter
    private static TransactionToReportEntryConvertor reportEntryConvertor;

    @Getter
    private static ServiceConfig serviceConfig;

    @Getter
    private static Repo repo;

    static {
        try {
            serviceConfig = new ServiceConfig();
            repo = new InMemoryRepo();

            transactionValidator = new TransactionValidatorImpl(repo);
            reportEntryConvertor = new TransactionToReportEntryConvertor();
        } catch (IOException ioException) {
            log.error("internal exception in creating beans", ioException);
        }
    }
}
