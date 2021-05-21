package com.example.transaction.validation.service;

import com.example.transaction.validation.TestHelper;
import com.example.transaction.validation.config.ServiceConfig;
import com.example.transaction.validation.convertor.TransactionToReportEntryConvertor;
import com.example.transaction.validation.helper.FileHelper;
import com.example.transaction.validation.repository.InMemoryRepo;
import com.example.transaction.validation.repository.Repo;
import com.example.transaction.validation.validators.TransactionValidatorImpl;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ValidationServiceImplIntegrationTest {

    private ValidationServiceImpl validationService;
    private static ServiceConfig serviceConfig;


    @BeforeAll
    static void beforeAll() throws IOException {
        serviceConfig = new ServiceConfig();
    }

    @AfterAll
    static void afterAll() throws IOException {
        FileHelper.DeleteDirectory(serviceConfig.getInputDir());
        FileHelper.DeleteDirectory(serviceConfig.getProcessedDir());
        FileHelper.DeleteDirectory(serviceConfig.getReportDir());
        FileHelper.DeleteDirectory(serviceConfig.getErrorDir());
    }

    @BeforeEach
    void setUp() {
        Repo repo = new InMemoryRepo();
        validationService = new ValidationServiceImpl(serviceConfig, new TransactionValidatorImpl(repo), new TransactionToReportEntryConvertor(), repo);
    }


    @Test
    void givenJsonFile_whenProcessFile_thenCorrectReport() throws IOException {
        // given
        final File jsonFile = TestHelper.createJsonFile(serviceConfig.getInputDir());

        // when
        validationService.processFile(jsonFile.toPath());

        // then
        assertTrue(Files.list(serviceConfig.getProcessedDir()).anyMatch(f -> f.getFileName().toString().contains(jsonFile.getName())));
        final Optional<Path> reportFile = Files.list(serviceConfig.getReportDir()).filter(f -> f.getFileName().toString().contains(jsonFile.getName())).findFirst();
        assertTrue(reportFile.isPresent());
        assertEquals(TestHelper.JSON_REPORT, Files.readString(reportFile.get()));
    }

    @Test
    void givenCsvFile_whenProcessFile_thenCorrectReport() throws IOException {
        // given
        final File csvFile = TestHelper.createCsvFile(serviceConfig.getInputDir());

        // when
        validationService.processFile(csvFile.toPath());

        // then
        assertTrue(Files.list(serviceConfig.getProcessedDir()).anyMatch(f -> f.getFileName().toString().contains(csvFile.getName())));
        final Optional<Path> reportFile = Files.list(serviceConfig.getReportDir()).filter(f -> f.getFileName().toString().contains(csvFile.getName())).findFirst();
        assertTrue(reportFile.isPresent());
        assertEquals(TestHelper.CSV_REPORT, Files.readString(reportFile.get()));
    }
}