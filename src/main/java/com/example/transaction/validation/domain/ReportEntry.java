package com.example.transaction.validation.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.*;

@Data
@Builder
public class ReportEntry {
    private static final ObjectMapper mapper = new ObjectMapper();

    private String reference;
    private String description;

    @SneakyThrows
    @Override
    public String toString() {
        return mapper.writeValueAsString(this);
    }
}
