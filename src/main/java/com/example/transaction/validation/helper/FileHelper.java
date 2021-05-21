package com.example.transaction.validation.helper;

import com.example.transaction.validation.constant.InputFormat;
import com.example.transaction.validation.exception.FileMoveException;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;

@Slf4j
public class FileHelper {

    public static Path move(final Path file, final Path destinationDir) {
        final Path dest = createUniqueFileName(file.getFileName(), destinationDir);

        try {
            Files.move(file, dest);
            return dest;

        } catch (FileAlreadyExistsException faee) {
            log.error("file already exist", faee);
            throw new FileMoveException("file already exist", file, dest);

        } catch (IOException ie) {
            log.error("exception on moving file", ie);
            throw new FileMoveException("exception on moving file", file, dest);
        }
    }

    public static Path createUniqueFileName(Path fileName, Path destinationDir) {
        return Path.of(String.format("%s/%s-%s", destinationDir, System.currentTimeMillis(), fileName));
    }

    public static Path createUniqueFileName(Path fileName, Path destinationDir, InputFormat format) {
        return Path.of(String.format("%s/%s-(%s)%s", destinationDir, System.currentTimeMillis(), fileName, format));
    }

    public static void DeleteDirectory(Path dir) throws IOException {
        Files.list(dir).forEach(f -> {
            try {
                Files.delete(f);
            } catch (IOException ioException) {
                log.error("exception on deleting directory", ioException);
            }
        });

        Files.delete(dir);
    }
}
