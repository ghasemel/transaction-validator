package com.example.transaction.validation.reader;

import com.example.transaction.validation.domain.Transaction;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import lombok.SneakyThrows;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;

public class JsonTransactionReader implements TransactionFileReader {

    private final JsonReader jsonReader;

    public JsonTransactionReader(Path filePath) throws IOException {
        InputStreamReader in = new InputStreamReader(Files.newInputStream(filePath));
        this.jsonReader = new JsonReader(in);
        this.jsonReader.beginArray();
    }

    @SneakyThrows
    @Override
    public boolean hasNext() {
        return jsonReader.hasNext();
    }

    @Override
    public Transaction readNext() {
        return new Gson().fromJson(jsonReader, Transaction.class);
    }

    @Override
    public void close() throws IOException {
        if (jsonReader != null)
            jsonReader.close();
    }
}
