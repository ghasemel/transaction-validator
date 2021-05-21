package com.example.transaction.validation.constant;

import com.example.transaction.validation.exception.InvalidFileFormat;

import java.nio.file.Path;

public enum InputFormat {
    CSV(".csv"),
    JSON(".json");

    private final String value;

    InputFormat(String value) {
        this.value = value;
    }

    public static InputFormat getFormatByFileName(Path filePath) {
        if (filePath.getFileName().toString().endsWith(".csv"))
            return InputFormat.CSV;

        else if (filePath.getFileName().toString().endsWith(".json")) {
            return InputFormat.JSON;

        } else {
            throw new InvalidFileFormat("acceptable file formats are csv and json", filePath);
        }
    }

    @Override
    public String toString() {
        return value;
    }
}
