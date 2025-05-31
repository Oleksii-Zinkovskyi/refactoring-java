package com.etraveli.practice.service.impl;

import com.etraveli.practice.dto.Customer;
import com.etraveli.practice.dto.MovieEnum;
import com.etraveli.practice.dto.MovieRental;
import com.etraveli.practice.dto.RentalInfoOutput;
import com.etraveli.practice.dto.RentalOwedAmount;
import com.etraveli.practice.helper.RentalInfoFormatter;
import com.etraveli.practice.service.RentalInfoService;
import com.google.common.util.concurrent.AtomicDouble;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class RentalInfoServiceImpl implements RentalInfoService {

    private static final int HUNDRED_YEARS_IN_DAYS = 365 * 100;

    private final RentalInfoFormatter rentalInfoFormatter;

    @Autowired
    public RentalInfoServiceImpl(RentalInfoFormatter stringRentalInfoFormatter) {
        this.rentalInfoFormatter = stringRentalInfoFormatter;
    }

    public String generateStatementForCustomer(Customer customer) {
        validateCustomer(customer);

        AtomicDouble totalAmountOwed = new AtomicDouble(0);
        AtomicInteger frequentEnterPoints = new AtomicInteger();
        List<RentalOwedAmount> rentalRecords = Collections.synchronizedList(new LinkedList<>());

        //TODO: Switch this to virtual threads with a custom Executor?
        //TODO: Verify for duplicate logic and control flows
        customer.rentals()
                .parallelStream()
                .forEach(movieRental -> {
                    double owedPerRental = processOwedAmountForRecord(movieRental);
                    totalAmountOwed.updateAndGet(v -> v + owedPerRental);
                    processFrequentEnterPoints(movieRental, frequentEnterPoints);
                    rentalRecords.add(new RentalOwedAmount(MovieEnum.valueOf(movieRental.movieId()).getTitle(), owedPerRental));
                });

        //Sorting to present the largest items contributing to the price first
        rentalRecords.sort(Comparator.comparing(RentalOwedAmount::owedAmount).reversed());

        return rentalInfoFormatter.formatOutput(
                RentalInfoOutput.builder()
                        .customerName(customer.name())
                        .rentalDebtRecords(rentalRecords)
                        .totalOwedAmount(totalAmountOwed.get())
                        .totalPoints(frequentEnterPoints.get())
                        .build());
    }

    private static void validateCustomer(Customer customer) {
        if (customer == null) {
            throw new IllegalArgumentException("Customer cannot be null");
        }
    }

    private static double processOwedAmountForRecord(MovieRental movieRental) {
        MovieEnum movie = validateMovieRecord(movieRental);

        //Magic numbers are kept here to match the original code's logic, since I have no idea as to their business meaning
        return switch (movie.getCode()) {
            case "regular" -> movieRental.days() > 2 ? (2 + (movieRental.days() - 2) * 1.5) : 2;
            case "new" -> movieRental.days() * 3;
            case "childrens" -> movieRental.days() > 3 ? (1.5 + (movieRental.days() - 3) * 1.5) : 1.5;
            default -> throw new IllegalArgumentException("Invalid movie code: " + movie.getCode());
        };
    }

    private static MovieEnum validateMovieRecord(MovieRental movieRental) {
        MovieEnum movie;
        try {
            movie = MovieEnum.valueOf(movieRental.movieId());
        } catch (IllegalArgumentException e) {
            //P.S. There could be sophisticated logic for handling this wherein we set an error flag but proceed with the rest of the rentals
            throw new IllegalArgumentException("Invalid movie ID: " + movieRental.movieId());
        }

        if (movieRental.days() < 0 || movieRental.days() > HUNDRED_YEARS_IN_DAYS) {
            throw new IllegalArgumentException("Invalid rental record for " + movieRental.movieId() + ", days value outside of bounds: " + movieRental.days());
        }
        return movie;
    }

    private static void processFrequentEnterPoints(MovieRental movieRental, AtomicInteger frequentEnterPoints) {
        if (MovieEnum.valueOf(movieRental.movieId()).getCode().equals("new") && movieRental.days() > 2) {
            frequentEnterPoints.getAndAdd(2);
        } else {
            frequentEnterPoints.getAndIncrement();
        }
    }

}
