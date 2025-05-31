package com.etraveli.practice.validator;

import com.etraveli.practice.dto.MovieEnum;
import com.etraveli.practice.dto.MovieRental;
import com.etraveli.practice.dto.ValidationResult;

public class MovieRecordValidator {

    private static final int A_HUNDRED_YEARS_IN_DAYS = 365 * 100;

    public static ValidationResult<MovieEnum> validateMovieRecord(MovieRental movieRental) {
        MovieEnum movie;
        try {
            //P.S. I thought about using an Optional here, but it looked ugly and served no purpose :(
            movie = MovieEnum.valueOf(movieRental.movieId());
        } catch (IllegalArgumentException e) {
            //P.S. There could be sophisticated logic for handling this wherein we set an error flag but proceed with the rest of the movieRentals
            return ValidationResult.failure("Invalid movie ID: " + movieRental.movieId());
        }

        if (movieRental.daysRented() < 0 || movieRental.daysRented() > A_HUNDRED_YEARS_IN_DAYS) {
            return ValidationResult.failure("Invalid rental record for " + movieRental.movieId() + ", daysRented value outside of bounds: " + movieRental.daysRented());
        }
        return ValidationResult.success(movie);
    }

}
