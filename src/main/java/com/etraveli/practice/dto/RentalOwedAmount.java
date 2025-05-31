package com.etraveli.practice.dto;

import lombok.NonNull;

public record RentalOwedAmount(
        @NonNull String movieTitle,
        @NonNull Double owedAmount) {

}
