package com.etraveli.practice.helper;

import com.etraveli.practice.dto.Customer;
import com.etraveli.practice.dto.MovieEnum;
import com.etraveli.practice.dto.MovieRental;

import java.util.concurrent.atomic.AtomicInteger;

public class RentalInfoHelper {

    private static final int HUNDRED_YEARS_IN_DAYS = 365 * 100;

    public static void validateCustomer(Customer customer) {
        if (customer == null) {
            throw new IllegalArgumentException("Customer cannot be null");
        }
    }

    public static double calculateDebtOwedForMovieRental(MovieRental movieRental) {
        MovieEnum movie = validateMovieRecord(movieRental);

        //Magic numbers are kept here to match the original code's logic, since I have no idea as to their business meaning
        return switch (movie.getCode()) {
            case "regular" -> movieRental.daysRented() > 2 ? (2 + (movieRental.daysRented() - 2) * 1.5) : 2;
            case "new" -> movieRental.daysRented() * 3;
            case "childrens" -> movieRental.daysRented() > 3 ? (1.5 + (movieRental.daysRented() - 3) * 1.5) : 1.5;
            default -> throw new IllegalArgumentException("Invalid movie code: " + movie.getCode());
        };
    }

    public static MovieEnum validateMovieRecord(MovieRental movieRental) {
        MovieEnum movie;
        try {
            movie = MovieEnum.valueOf(movieRental.movieId());
        } catch (IllegalArgumentException e) {
            //P.S. There could be sophisticated logic for handling this wherein we set an error flag but proceed with the rest of the movieRentals
            throw new IllegalArgumentException("Invalid movie ID: " + movieRental.movieId());
        }

        if (movieRental.daysRented() < 0 || movieRental.daysRented() > HUNDRED_YEARS_IN_DAYS) {
            throw new IllegalArgumentException("Invalid rental record for " + movieRental.movieId() + ", daysRented value outside of bounds: " + movieRental.daysRented());
        }
        return movie;
    }

    public static void accumulateFrequentPoints(MovieRental movieRental, AtomicInteger totalFrequentPoints) {
        if (MovieEnum.valueOf(movieRental.movieId()).getCode().equals("new") && movieRental.daysRented() > 2) {
            totalFrequentPoints.getAndAdd(2);
        } else {
            totalFrequentPoints.getAndIncrement();
        }
    }

}
