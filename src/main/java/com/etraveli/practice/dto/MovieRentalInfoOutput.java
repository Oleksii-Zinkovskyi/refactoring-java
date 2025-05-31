package com.etraveli.practice.dto;

import lombok.Builder;
import lombok.NonNull;

import java.util.List;

@Builder
public record MovieRentalInfoOutput(
        @NonNull String customerName,
        @NonNull List<MovieRentalDebtRecord> movieRentalDebtRecords,
        @NonNull Double totalDebtAmountOwed,
        @NonNull Integer totalFrequentPoints) {

}