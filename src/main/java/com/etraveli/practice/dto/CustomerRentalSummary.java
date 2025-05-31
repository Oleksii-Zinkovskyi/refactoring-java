package com.etraveli.practice.dto;

import com.google.common.util.concurrent.AtomicDouble;
import lombok.NonNull;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public record CustomerRentalSummary(
        @NonNull AtomicDouble totalDebtAmountOwed,
        @NonNull AtomicInteger totalFrequentPoints,
        @NonNull List<MovieRentalDebtRecord> movieRentalDebtRecords) {

}
