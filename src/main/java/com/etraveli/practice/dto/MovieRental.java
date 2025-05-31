package com.etraveli.practice.dto;

import lombok.NonNull;

public record MovieRental (
        @NonNull String movieId,
        @NonNull Integer days) {

}
