package com.etraveli.practice.validator;

import com.etraveli.practice.dto.MovieRental;
import com.etraveli.practice.dto.ValidationResult;

public class MovieRecordValidator {

    private static final int A_HUNDRED_YEARS_IN_DAYS = 365 * 100;

    public static ValidationResult<MovieRental> validateMovieRecord(MovieRental movieRental) {
        if (movieRental.daysRented() < 0 || movieRental.daysRented() > A_HUNDRED_YEARS_IN_DAYS) {
            return ValidationResult.failure("Invalid rental record for " + movieRental.movieId() + ", daysRented value outside of bounds: " + movieRental.daysRented());
        }
        return ValidationResult.success(movieRental);
    }

}
