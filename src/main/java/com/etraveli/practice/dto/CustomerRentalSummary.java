package com.etraveli.practice.dto;

import lombok.NonNull;

import java.util.List;

public record CustomerRentalSummary(
        @NonNull Double totalDebtAmountOwed,
        @NonNull Integer totalFrequentPoints,
        @NonNull List<MovieRentalDebtRecord> movieRentalDebtRecords) {

}
