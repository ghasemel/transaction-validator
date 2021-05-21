package com.example.transaction.validation.service;

import com.example.transaction.validation.config.ServiceConfig;
import com.example.transaction.validation.constant.InputFormat;
import com.example.transaction.validation.convertor.TransactionToReportEntryConvertor;
import com.example.transaction.validation.domain.ReportEntry;
import com.example.transaction.validation.domain.Transaction;
import com.example.transaction.validation.exception.InvalidFileFormat;
import com.example.transaction.validation.helper.FileHelper;
import com.example.transaction.validation.reader.CsvTransactionReader;
import com.example.transaction.validation.reader.JsonTransactionReader;
import com.example.transaction.validation.reader.TransactionFileReader;
import com.example.transaction.validation.repository.Repo;
import com.example.transaction.validation.validators.TransactionValidator;
import lombok.extern.slf4j.Slf4j;

import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Stream;

@Slf4j
public class ValidationServiceImpl implements ValidationService {
    private Timer timer;
    private final ServiceConfig serviceConfig;
    private final TransactionValidator transactionValidator;
    private final TransactionToReportEntryConvertor reportEntryConvertor;
    private final Repo repo;

    public ValidationServiceImpl(ServiceConfig serviceConfig, TransactionValidator transactionValidator, TransactionToReportEntryConvertor reportEntryConvertor, Repo repo) {
        this.serviceConfig = serviceConfig;
        this.transactionValidator = transactionValidator;
        this.reportEntryConvertor = reportEntryConvertor;
        this.repo = repo;
    }

    public void startService() {
        log.info("service started");
        timer = new Timer();
        timer.schedule(getTask(), 0, serviceConfig.getInputInterval());
    }

    public void stopService() {
        log.info("service stopped");
        if (timer != null)
            timer.cancel();
    }

    protected void checkInput() {
        log.info("checking input directory [{}]...", serviceConfig.getInputDir());
        try (final Stream<Path> fileList = Files.list(serviceConfig.getInputDir())) {
            fileList.forEach(this::processFile);

        } catch (InvalidFileFormat iff) {
            log.error(iff.getMessage());
            moveToError(iff.getFile());
        } catch (Exception e) {
            log.error("exception", e);
        }
    }

    protected void processFile(Path inputFile) {
        InputFormat inputFormat = InputFormat.getFormatByFileName(inputFile);

        try (TransactionFileReader transactionReader = (inputFormat == InputFormat.CSV) ?
                new CsvTransactionReader(inputFile) :
                new JsonTransactionReader(inputFile)
        ) {

            final Optional<String> processResultOpt = processTransactions(transactionReader);
            log.debug("Transaction file [{}] has been processed.", inputFile.getFileName());

            if (processResultOpt.isPresent()) {
                final Path reportFile = generateReportFileName(inputFile);
                saveToReportFile(reportFile, processResultOpt.get());
            }

            moveToProcessed(inputFile);

        } catch (Exception ioe) {
            log.error("exception on processing inputFile", ioe);
            moveToError(inputFile);
        }
    }


    // region private methods
    private Optional<String> processTransactions(final TransactionFileReader transactionReader) {
        StringBuilder sb = new StringBuilder();
        while (transactionReader.hasNext()) {
            final Transaction transaction = transactionReader.readNext();
            final Optional<String> invalidTrans = processTransaction(transaction);

            if (invalidTrans.isPresent()) {
                if (sb.length() > 0)
                    sb.append(",\n");
                sb.append(invalidTrans.get());
            } else {
                repo.add(transaction);
            }
        }
        if (sb.length() == 0)
            return Optional.empty();
        return Optional.of(sb.toString());
    }

    private Optional<String> processTransaction(final Transaction transaction) {
        final Optional<ReportEntry> validationResult = validateTransaction(transaction);
        return validationResult.map(ReportEntry::toString);
    }

    private Optional<ReportEntry> validateTransaction(final Transaction tran) {
        if (!transactionValidator.isUnique(tran) || !transactionValidator.ValidateBalance(tran)) {
            log.debug("transaction '{}' in not valid", tran);
            return reportEntryConvertor.convert(tran);
        }
        return Optional.empty();
    }

    private TimerTask getTask() {
        return new TimerTask() {
            @Override
            public void run() {
                checkInput();
            }
        };
    }

    private Path generateReportFileName(Path inputFile) {
        final Path reportFile = FileHelper.createUniqueFileName(inputFile.getFileName(), serviceConfig.getReportDir(), InputFormat.JSON);
        log.info("there are invalid transactions in [{}], result in [{}]", inputFile, reportFile);
        return reportFile;
    }

    private void saveToReportFile(Path reportFile, String content) throws IOException {
        try (OutputStreamWriter out = new FileWriter(reportFile.toFile())) {
            out.write("[\n");
            out.write(content);
            out.write("\n]");
        }
        log.debug("report [{}] has been successfully created.", reportFile);
    }

    private void moveToProcessed(Path inputFile) {
        final Path moved = FileHelper.move(inputFile, serviceConfig.getProcessedDir());
        log.debug("file [{}] moved to [{}]", inputFile, moved);
    }

    private void moveToError(Path inputFile) {
        final Path moved = FileHelper.move(inputFile, serviceConfig.getErrorDir());
        log.error("file [{}] moved to [{}]", inputFile, moved);
    }
    // endregion private methods
}
