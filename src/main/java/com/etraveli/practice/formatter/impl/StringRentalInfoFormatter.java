package com.etraveli.practice.formatter.impl;

import com.etraveli.practice.dto.MovieRentalInfoOutput;
import com.etraveli.practice.dto.MovieRentalDebtRecord;
import com.etraveli.practice.formatter.RentalInfoFormatter;
import org.springframework.stereotype.Component;

@Component
public class StringRentalInfoFormatter implements RentalInfoFormatter {

    public String formatOutput(MovieRentalInfoOutput output) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Rental Record for ").append(output.customerName()).append("\n");

        for (MovieRentalDebtRecord debtRecord : output.movieRentalDebtRecords()) {
            stringBuilder.append("\t").append(debtRecord.movieTitle()).append("\t").append(debtRecord.debtOwedForTitle()).append("\n");
        }

        stringBuilder.append("Amount owed is ").append(output.totalDebtAmountOwed()).append("\n");
        stringBuilder.append("You earned ").append(output.totalFrequentPoints()).append(" frequent points\n");

        return stringBuilder.toString();
    }

}
