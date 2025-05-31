package com.etraveli.practice.dto;

import lombok.Builder;
import lombok.NonNull;

import java.util.List;

@Builder
public record RentalInfoOutput(
        @NonNull String customerName,
        @NonNull List<RentalOwedAmount> rentalDebtRecords,
        @NonNull Double totalOwedAmount,
        @NonNull Integer totalPoints) {

}