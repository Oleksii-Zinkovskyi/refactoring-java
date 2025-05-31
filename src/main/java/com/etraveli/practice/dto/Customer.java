package com.etraveli.practice.dto;

import lombok.NonNull;

import java.util.List;

public record Customer (
        @NonNull String name,
        @NonNull List<MovieRental> rentals) {

}
