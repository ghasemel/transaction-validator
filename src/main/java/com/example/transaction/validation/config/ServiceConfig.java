package com.example.transaction.validation.config;

import lombok.Getter;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

@Getter
public class ServiceConfig {
    private final Path inputDir;
    private final Path reportDir;
    private final Path processedDir;
    private final Path errorDir;
    private final long inputInterval;

    public ServiceConfig() throws IOException {
        try (InputStream input = ServiceConfig.class.getClassLoader().getResourceAsStream("config.properties")) {

            Properties prop = new Properties();
            prop.load(input);

            inputDir = Path.of(prop.getProperty("app.input.dir"));
            reportDir = Path.of(prop.getProperty("app.report.dir"));
            processedDir = Path.of(prop.getProperty("app.processed.dir"));
            errorDir = Path.of(prop.getProperty("app.error.dir"));
            inputInterval = Long.parseLong(prop.getProperty("app.input.interval"));

            Files.createDirectories(inputDir);
            Files.createDirectories(reportDir);
            Files.createDirectories(processedDir);
            Files.createDirectories(errorDir);
        }
    }
}
