package com.etraveli.practice.helper.impl;

import com.etraveli.practice.dto.RentalInfoOutput;
import com.etraveli.practice.dto.RentalOwedAmount;
import com.etraveli.practice.helper.RentalInfoFormatter;
import org.springframework.stereotype.Component;

@Component
public class StringRentalInfoFormatter implements RentalInfoFormatter {

    public String formatOutput(RentalInfoOutput output) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Rental Record for ").append(output.customerName()).append("\n");

        for (RentalOwedAmount debtRecord : output.rentalDebtRecords()) {
            stringBuilder.append("\t").append(debtRecord.movieTitle()).append("\t").append(debtRecord.owedAmount()).append("\n");
        }

        stringBuilder.append("Amount owed is ").append(output.totalOwedAmount()).append("\n");
        stringBuilder.append("You earned ").append(output.totalPoints()).append(" frequent points\n");

        return stringBuilder.toString();
    }

}
