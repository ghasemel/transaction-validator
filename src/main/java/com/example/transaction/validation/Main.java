package com.example.transaction.validation;

import com.example.transaction.validation.config.BeanConfig;
import com.example.transaction.validation.service.ValidationService;
import com.example.transaction.validation.service.ValidationServiceImpl;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Main {

    public static void main(String[] args) {
        log.info("app started");

        // create validation service
        ValidationService validationService = new ValidationServiceImpl(
                BeanConfig.getServiceConfig(),
                BeanConfig.getTransactionValidator(),
                BeanConfig.getReportEntryConvertor(),
                BeanConfig.getRepo());

        // run validation service
        try {
            validationService.startService();

        } catch (Exception e) {
            validationService.stopService();
            log.error("internal exception", e);
        }
    }
}
