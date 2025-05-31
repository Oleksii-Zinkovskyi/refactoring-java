package com.etraveli.practice.dto;

public record ValidationResult<T>(boolean valid, T value, String errorMessage) {

    public static <T> ValidationResult<T> success(T value) {
        return new ValidationResult<>(true, value, null);
    }

    public static <T> ValidationResult<T> failure(String errorMessage) {
        return new ValidationResult<>(false, null, errorMessage);
    }

    //P.S. Not used in this example, but is good to have for completeness (KISS/YAGNI disagree)
    public void throwIfInvalid() {
        if (!valid) {
            throw new IllegalStateException("Validation failed: " + errorMessage);
        }
    }

}
