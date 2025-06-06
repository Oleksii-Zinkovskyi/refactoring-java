package com.etraveli.practice.dto;

import lombok.NonNull;

public record Movie(
        @NonNull String title,
        @NonNull MovieCode code) {
}
