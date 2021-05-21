package com.example.transaction.validation.exception;

import lombok.Getter;

import java.nio.file.Path;

public class InvalidFileFormat extends RuntimeException {
    @Getter
    private Path file;

    public InvalidFileFormat(String message, Path file) {
        super(message);
        this.file = file;
    }

    @Override
    public String toString() {
        return "InvalidFileFormat{" +
                "message=" + getMessage() + "," +
                "file=" + file +
                '}';
    }
}
