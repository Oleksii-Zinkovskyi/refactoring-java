package com.etraveli.practice.util;

import com.etraveli.practice.dto.*;
import com.etraveli.practice.validator.MovieRecordValidator;

import java.util.Comparator;
import java.util.function.BinaryOperator;
import java.util.stream.Stream;

public class MovieRentalUtils {

    /**
     * Calculates the total debt owed for a movie rental based on the movie type and days rented.
     *
     * @param movieRental The movie rental details containing movie ID and days rented.
     * @param movie       The movie details containing the movie code.
     * @return The total debt owed for the supplied movie rental.
     */
    public static Double calculateDebtOwedForMovieRental(MovieRental movieRental, Movie movie) {
        performMovieValidation(movieRental);

        //P.S. Magic numbers are kept to match the original code's logic, since I can only guess as to their business meaning
        return switch (movie.code()) {
            case REGULAR -> movieRental.daysRented() > 2 ? (2 + (movieRental.daysRented() - 2) * 1.5) : 2;
            case NEW -> movieRental.daysRented() * 3.0;
            case CHILDREN -> movieRental.daysRented() > 3 ? (1.5 + (movieRental.daysRented() - 3) * 1.5) : 1.5;
        };
    }

    /**
     * Calculates the frequent points for a movie rental based on the movie type and days rented.
     *
     * @param movieRental The movie rental details containing movie ID and days rented.
     * @param movie       The movie details containing the movie code.
     * @return The frequent points earned for the supplied movie rental.
     */
    public static Integer calculateFrequentPoints(MovieRental movieRental, Movie movie) {
        if (movie.code().equals(MovieCode.NEW) && movieRental.daysRented() > 2) {
            return 2;
        } else {
            return 1;
        }
    }

    /**
     * Merges two CustomerRentalSummary objects into one.
     *
     * @return A BinaryOperator that merges two CustomerRentalSummary objects.
     */
    public static BinaryOperator<CustomerRentalSummary> mergeCustomerRentalSummaries() {
        return (a, b) -> new CustomerRentalSummary(
                a.totalDebtAmountOwed() + b.totalDebtAmountOwed(),
                a.totalFrequentPoints() + b.totalFrequentPoints(),
                Stream.concat(a.movieRentalDebtRecords().stream(), b.movieRentalDebtRecords().stream()).toList()
        );
    }

    /**
     * Sorts the CustomerRentalSummary by the debt owed for each movie title in descending order.
     *
     * @param unsortedRentalSummary The unsorted CustomerRentalSummary to be sorted.
     * @return A new CustomerRentalSummary with movie rental debt records sorted by debt owed in descending order.
     */
    public static CustomerRentalSummary sortCustomerRentalSummary(CustomerRentalSummary unsortedRentalSummary) {
        return new CustomerRentalSummary(unsortedRentalSummary.totalDebtAmountOwed(),
                unsortedRentalSummary.totalFrequentPoints(),
                unsortedRentalSummary.movieRentalDebtRecords()
                        .stream()
                        .sorted(Comparator.comparing(MovieRentalDebtRecord::debtOwedForTitle).reversed())
                        .toList());
    }

    private static void performMovieValidation(MovieRental movieRental) {
        ValidationResult<MovieRental> movieRentalValidation = MovieRecordValidator.validateMovieRecord(movieRental);
        if (!movieRentalValidation.valid()) {
            throw new IllegalArgumentException(movieRentalValidation.errorMessage());
        }
    }

}
