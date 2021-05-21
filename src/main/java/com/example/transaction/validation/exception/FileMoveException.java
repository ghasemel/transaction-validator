package com.example.transaction.validation.exception;

import lombok.Getter;

import java.nio.file.Path;

public class FileMoveException extends RuntimeException {
    @Getter
    private final Path file;

    @Getter
    private final Path destination;

    public FileMoveException(String message, Path file, Path destination) {
        super(message);
        this.file = file;
        this.destination = destination;
    }

    @Override
    public String toString() {
        return "FileMoveException{" +
                "message=" + getMessage() + "," +
                "file=" + file + "," +
                "destination=" + destination +
                '}';
    }
}
