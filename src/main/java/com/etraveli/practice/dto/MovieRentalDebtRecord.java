package com.etraveli.practice.dto;

import lombok.NonNull;

public record MovieRentalDebtRecord(
        @NonNull String movieTitle,
        @NonNull Double debtOwedForTitle) {

}
