package com.etraveli.practice.service.impl;

import com.etraveli.practice.dto.*;
import com.etraveli.practice.formatter.RentalInfoFormatter;
import com.etraveli.practice.repository.MovieRepository;
import com.etraveli.practice.util.CustomerUtils;
import com.etraveli.practice.util.MovieRentalUtils;
import com.etraveli.practice.service.RentalInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
public class RentalInfoServiceImpl implements RentalInfoService {

    private final RentalInfoFormatter rentalInfoFormatter;
    private final MovieRepository movieRepository;

    @Autowired
    public RentalInfoServiceImpl(RentalInfoFormatter stringRentalInfoFormatter, MovieRepository mockMovieRepository) {
        this.rentalInfoFormatter = stringRentalInfoFormatter;
        this.movieRepository = mockMovieRepository;
    }

    public String generateStatementForCustomer(Customer customer) {
        CustomerUtils.performCustomerValidation(customer);
        CustomerRentalSummary customerRentalSummary = populateCustomerRentalSummary(customer);
        return buildOutput(customer, MovieRentalUtils.sortCustomerRentalSummary(customerRentalSummary));
    }

    //P.S. Parallel stream here makes little sense due to the overhead of creating and joining those threads. Keeping it here for demonstration purposes.
    private CustomerRentalSummary populateCustomerRentalSummary(Customer customer) {
        return customer.movieRentals()
                .parallelStream()
                .map(this::processMovieRentalRecord)
                .reduce(
                        new CustomerRentalSummary(0d, 0, new LinkedList<>()),
                        MovieRentalUtils.mergeCustomerRentalSummaries()
                );
    }

    private CustomerRentalSummary processMovieRentalRecord(MovieRental movieRental) {
        Movie movie = getMovieById(movieRental.movieId());
        Double debtOwedForTitle = MovieRentalUtils.calculateDebtOwedForMovieRental(movieRental, movie);
        Integer frequentPoints = MovieRentalUtils.calculateFrequentPoints(movieRental, movie);
        MovieRentalDebtRecord movieDebtRecord = new MovieRentalDebtRecord(movie.title(), debtOwedForTitle);
        return new CustomerRentalSummary(debtOwedForTitle, frequentPoints, List.of(movieDebtRecord));   //P.S. This could have been a different type to avoid an extra List here
    }

    private Movie getMovieById(String movieId) {
        Movie movie = movieRepository.getMovieById(movieId);
        if (movie == null) {
            throw new IllegalArgumentException("Invalid movie ID: " + movieId);
        }
        return movie;
    }

    private String buildOutput(Customer customer, CustomerRentalSummary customerRentalSummary) {
        return rentalInfoFormatter
                .formatOutput(MovieRentalInfoOutput.builder()
                        .customerName(customer.name())
                        .movieRentalDebtRecords(customerRentalSummary.movieRentalDebtRecords())
                        .totalDebtAmountOwed(customerRentalSummary.totalDebtAmountOwed())
                        .totalFrequentPoints(customerRentalSummary.totalFrequentPoints())
                        .build());
    }

}
