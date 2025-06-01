package com.etraveli.practice.service.impl;

import com.etraveli.practice.dto.Customer;
import com.etraveli.practice.dto.CustomerRentalSummary;
import com.etraveli.practice.dto.MovieEnum;
import com.etraveli.practice.dto.MovieRental;
import com.etraveli.practice.dto.MovieRentalDebtRecord;
import com.etraveli.practice.dto.MovieRentalInfoOutput;
import com.etraveli.practice.formatter.RentalInfoFormatter;
import com.etraveli.practice.util.CustomerUtils;
import com.etraveli.practice.util.MovieRentalUtils;
import com.etraveli.practice.service.RentalInfoService;
import com.google.common.util.concurrent.AtomicDouble;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class RentalInfoServiceImpl implements RentalInfoService {

    private final RentalInfoFormatter rentalInfoFormatter;

    @Autowired
    public RentalInfoServiceImpl(RentalInfoFormatter stringRentalInfoFormatter) {
        this.rentalInfoFormatter = stringRentalInfoFormatter;
    }

    public String generateStatementForCustomer(Customer customer) {
        CustomerUtils.performCustomerValidation(customer);
        CustomerRentalSummary customerRentalSummary = processMovieRentalRecords(customer);  //P.S. Temp variable for the sake of readability
        return buildOutput(customer, customerRentalSummary);
    }

    /*P.S. The use of vars ("var name = new ...") over explicit types is down to personal preference and project guidelines. I am demonstrating it here for the sake of readability and conciseness.
    And since we already have Lombok, we can even use "val" to make it equivalent to "final var"! In all honesty, I should have been declaring more of the fields as final, but was a bit lazy...*/
    public static CustomerRentalSummary processMovieRentalRecords(Customer customer) {
        val totalDebtAmountOwed = new AtomicDouble(0);
        val totalFrequentPoints = new AtomicInteger(0);
        val movieRentalDebtRecords = Collections.synchronizedList(new LinkedList<MovieRentalDebtRecord>());

        customer.movieRentals()
                .parallelStream()
                .forEach(movieRental -> populateMovieRentalDebtRecords(movieRental, totalDebtAmountOwed, totalFrequentPoints, movieRentalDebtRecords));

        // Sort the records by debt owed in descending order, ensuring that titles with the most debt are listed first
        movieRentalDebtRecords.sort(Comparator.comparing(MovieRentalDebtRecord::debtOwedForTitle).reversed());
        return new CustomerRentalSummary(totalDebtAmountOwed, totalFrequentPoints, movieRentalDebtRecords);
    }

    public static void populateMovieRentalDebtRecords(MovieRental movieRental, AtomicDouble totalDebtAmountOwed, AtomicInteger totalFrequentPoints, List<MovieRentalDebtRecord> movieRentalDebtRecords) {
        val debtOwedForTitle = MovieRentalUtils.calculateDebtOwedForMovieRental(movieRental);
        totalDebtAmountOwed.getAndAdd(debtOwedForTitle);
        totalFrequentPoints.getAndAdd(MovieRentalUtils.calculateFrequentPoints(movieRental));
        movieRentalDebtRecords.add(new MovieRentalDebtRecord(MovieEnum.valueOf(movieRental.movieId()).getTitle(), debtOwedForTitle));
    }

    private String buildOutput(Customer customer, CustomerRentalSummary customerRentalSummary) {
        return rentalInfoFormatter
                .formatOutput(MovieRentalInfoOutput.builder()
                        .customerName(customer.name())
                        .movieRentalDebtRecords(customerRentalSummary.movieRentalDebtRecords())
                        .totalDebtAmountOwed(customerRentalSummary.totalDebtAmountOwed().get())
                        .totalFrequentPoints(customerRentalSummary.totalFrequentPoints().get())
                        .build());
    }

}
