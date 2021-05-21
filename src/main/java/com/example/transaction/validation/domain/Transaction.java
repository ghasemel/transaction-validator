package com.example.transaction.validation.domain;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.opencsv.bean.CsvBindByName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@JsonPropertyOrder(alphabetic = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {
    @CsvBindByName(column = "Reference", required = true)
    private String reference;

    @CsvBindByName(column = "AccountNumber")
    private String accountNumber;

    @CsvBindByName(column = "Description", required = true)
    private String description;

    @CsvBindByName(column = "Start Balance", required = true)
    private BigDecimal startBalance;

    @CsvBindByName(column = "Mutation", required = true)
    private BigDecimal mutation;

    @CsvBindByName(column = "End Balance", required = true)
    private BigDecimal endBalance;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Transaction that = (Transaction) o;

        return reference.equals(that.reference);
    }

    @Override
    public int hashCode() {
        return reference.hashCode();
    }
}
