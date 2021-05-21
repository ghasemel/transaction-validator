package com.example.transaction.validation.service;

import com.example.transaction.validation.config.ServiceConfig;
import com.example.transaction.validation.convertor.TransactionToReportEntryConvertor;
import com.example.transaction.validation.exception.InvalidFileFormat;
import com.example.transaction.validation.helper.FileHelper;
import com.example.transaction.validation.repository.Repo;
import com.example.transaction.validation.validators.TransactionValidator;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ValidationServiceImplTest {

    @InjectMocks
    @Spy
    private ValidationServiceImpl validationService;

    @Mock
    private ServiceConfig config;

    @Mock
    private TransactionValidator transactionValidator;

    @Mock
    private TransactionToReportEntryConvertor reportEntryConvertor;

    @Mock
    private Repo repo;

    private static final Path INPUT_DIR = Path.of("./in-tmp/");
    private static final Path PROCESSED_DIR = Path.of("./processed-tmp/");
    private static final Path REPORT_DIR = Path.of("./report-tmp/");
    private static final Path ERROR_DIR = Path.of("./error-tmp/");

    @BeforeAll
    static void beforeAll() throws IOException {
        Files.createDirectories(INPUT_DIR);
        Files.createDirectories(PROCESSED_DIR);
        Files.createDirectories(REPORT_DIR);
        Files.createDirectories(ERROR_DIR);
    }

    @AfterAll
    static void afterAll() throws IOException {
        FileHelper.DeleteDirectory(INPUT_DIR);
        FileHelper.DeleteDirectory(PROCESSED_DIR);
        FileHelper.DeleteDirectory(REPORT_DIR);
        FileHelper.DeleteDirectory(ERROR_DIR);
    }

    @BeforeEach
    void setUp() {
    }

    @Test
    void givenEmptyInputDir_whenCheckInput_thenNoCallToProcessFile() {
        // given
        when(config.getInputDir()).thenReturn(INPUT_DIR);

        // when
        validationService.checkInput();

        // then
        verify(validationService, never()).processFile(any());
    }

    @Test
    void givenInvalidSampleInputFile_whenCheckInput_thenMoveFileToErrorDir() throws IOException {
        // given
        when(config.getInputDir()).thenReturn(INPUT_DIR);
        when(config.getErrorDir()).thenReturn(ERROR_DIR);

        final File sample = File.createTempFile("sample", ".tmp", INPUT_DIR.toFile());
        doThrow(new InvalidFileFormat("invalid", sample.toPath())).when(validationService).processFile(sample.toPath());

        // when
        validationService.checkInput();

        // then
        verify(validationService).processFile(sample.toPath());
        assertTrue(Files.list(ERROR_DIR).anyMatch(f -> f.getFileName().toString().contains(sample.getName())));

    }
}