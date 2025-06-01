package com.etraveli.practice.util;

import com.etraveli.practice.dto.MovieEnum;
import com.etraveli.practice.dto.MovieRental;
import com.etraveli.practice.dto.ValidationResult;
import com.etraveli.practice.validator.MovieRecordValidator;

public class MovieRentalUtils {

    /**
     * Calculates the total debt owed for a movie rental based on the movie type and days rented.
     *
     * @param movieRental The movie rental details containing movie ID and days rented.
     * @return The total debt owed for the supplied movie rental.
     */
    public static Double calculateDebtOwedForMovieRental(MovieRental movieRental) {
        MovieEnum movie = performMovieValidation(movieRental).value();

        //P.S. Magic numbers are kept to match the original code's logic, since I can only guess as to their business meaning
        return switch (movie.getCode()) {
            case "regular" -> movieRental.daysRented() > 2 ? (2 + (movieRental.daysRented() - 2) * 1.5) : 2;
            case "new" -> movieRental.daysRented() * 3.0;
            case "childrens" -> movieRental.daysRented() > 3 ? (1.5 + (movieRental.daysRented() - 3) * 1.5) : 1.5;
            default -> throw new IllegalArgumentException("Invalid movie code: " + movie.getCode());
        };
    }

    /**
     * Calculates the frequent points for a movie rental based on the movie type and days rented.
     *
     * @param movieRental The movie rental details containing movie ID and days rented.
     * @return The frequent points earned for the supplied movie rental.
     */
    public static Integer calculateFrequentPoints(MovieRental movieRental) {
        if (MovieEnum.valueOf(movieRental.movieId()).getCode().equals("new") && movieRental.daysRented() > 2) {
            return 2;
        } else {
            return 1;
        }
    }

    private static ValidationResult<MovieEnum> performMovieValidation(MovieRental movieRental) {
        ValidationResult<MovieEnum> movieValidation = MovieRecordValidator.validateMovieRecord(movieRental);
        if (!movieValidation.valid()) {
            throw new IllegalArgumentException(movieValidation.errorMessage());
        }
        return movieValidation;
    }

}
