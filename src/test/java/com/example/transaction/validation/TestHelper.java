package com.example.transaction.validation;

import com.example.transaction.validation.constant.InputFormat;
import com.example.transaction.validation.domain.Transaction;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;

public class TestHelper {

    public static File createJsonFile(Path dir) throws IOException {
        return createFile(dir, InputFormat.JSON.toString(), JSON_CONTENT);
    }

    public static File createCsvFile(Path dir) throws IOException {
        return createFile(dir, InputFormat.CSV.toString(), CSV_CONTENT);
    }

    public static File createFile(Path dir, String suffix, String content) throws IOException {
        final File sample = File.createTempFile("sample", suffix, dir.toFile());

        try (var writer = new FileWriter(sample)) {
            writer.write(content);
        }

        return sample;
    }

    private static final String CSV_CONTENT = "Reference,AccountNumber,Description,Start Balance,Mutation,End Balance\n" +
            "194261,NL91RABO0315273637,Book John Smith,21.6,-41.83,-20.23\n" +
            "112806,NL27SNSB0917829871,Clothes Irma Steven,91.23,+15.57,106.8\n" +
            "183049,NL69ABNA0433647324,Book Arndt Thilo,86.66,+44.5,131.16\n" +
            "183356,NL74ABNA0248990274,Toy Jimmie Clarice,92.98,-46.65,46.33\n" +
            "112806,NL69ABNA0433647324,Book Peter de Vries,90.83,-10.91,79.92\n" +
            "112806,NL93ABNA0585619023,Book Richard Tyson,102.12,+45.87,147.99\n" +
            "139524,NL43AEGO0773393871,Flowers Jan Tyson,99.44,+41.23,140.67\n" +
            "179430,NL93ABNA0585619023,Clothes Dolores Kerensa,23.96,-27.43,-3.47\n" +
            "141223,NL93ABNA0585619023,Book Mahala Kreszenz,94.25,+41.6,135.85\n" +
            "195446,NL74ABNA0248990274,Toy Hal Shavonne,26.32,+48.98,75.3\n";

    public static final String CSV_REPORT = "[\n" +
            "{\"reference\":\"112806\",\"description\":\"Clothes Irma Steven\"},\n" +
            "{\"reference\":\"112806\",\"description\":\"Book Richard Tyson\"},\n" +
            "{\"reference\":\"195446\",\"description\":\"Toy Hal Shavonne\"}\n" +
            "]";


    private static final String JSON_CONTENT = "[\n" +
            trans1ValidBody() +
            ",\n" +
            trans1InvalidBody() +
            ",\n" +
            trans2ValidBody() +
            ",\n" +
            trans2InvalidBody() +
            "\n" +
            "]";

    private static final ObjectMapper mapper = new ObjectMapper();
    public static final Transaction TRANS1_INVALID = createTransaction(trans1InvalidBody());
    public static final Transaction TRANS2_INVALID = createTransaction(trans2InvalidBody());
    public static final Transaction TRANS1_VALID = createTransaction(trans1ValidBody());
    public static final Transaction TRANS2_VALID = createTransaction(trans2ValidBody());
    public static final String JSON_REPORT = "[\n" +
            "{\"reference\":\"" + TRANS1_INVALID.getReference() + "\",\"description\":\"" + TRANS1_INVALID.getDescription() + "\"},\n" +
            "{\"reference\":\"" + TRANS2_INVALID.getReference() + "\",\"description\":\"" + TRANS2_INVALID.getDescription() + "\"}\n" +
            "]";


    private static String trans1InvalidBody() {
        return "{\n" +
                "      \"reference\": \"167875\",\n" +
                "      \"accountNumber\": \"NL93ABNA0585619023\",\n" +
                "      \"description\": \"Toy Greg Alysha\",\n" +
                "      \"startBalance\": 5429,\n" +
                "      \"mutation\": -939,\n" +
                "      \"endBalance\": 6368\n" +
                "   }";
    }

    private static String trans2InvalidBody() {
        return "   {\n" +
                "      \"reference\": \"165102\",\n" +
                "      \"accountNumber\": \"NL93ABNA0585619023\",\n" +
                "      \"description\": \"Book Shevaun Taylor\",\n" +
                "      \"startBalance\": 3980,\n" +
                "      \"mutation\": 1000,\n" +
                "      \"endBalance\": 4981\n" +
                "   }";
    }

    private static String trans1ValidBody() {
        return "   {\n" +
                "      \"reference\": \"130498\",\n" +
                "      \"accountNumber\": \"NL69ABNA0433647324\",\n" +
                "      \"description\": \"Book Jan Theu\",\n" +
                "      \"startBalance\": 26.9,\n" +
                "      \"mutation\": -18.78,\n" +
                "      \"endBalance\": 8.12\n" +
                "   }";
    }

    private static String trans2ValidBody() {
        return "   {\n" +
                "      \"reference\": \"150438\",\n" +
                "      \"accountNumber\": \"NL74ABNA0248990274\",\n" +
                "      \"description\": \"Toy Jan de Vries\",\n" +
                "      \"startBalance\": 10.1,\n" +
                "      \"mutation\": -0.3,\n" +
                "      \"endBalance\": 9.8\n" +
                "   }";
    }

    private static Transaction createTransaction(String body) {
        try {
            return mapper.readValue(body, Transaction.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }
}
