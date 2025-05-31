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

    public static CustomerRentalSummary processMovieRentalRecords(Customer customer) {
        AtomicDouble totalDebtAmountOwed = new AtomicDouble(0);
        AtomicInteger totalFrequentPoints = new AtomicInteger();
        List<MovieRentalDebtRecord> movieRentalDebtRecords = Collections.synchronizedList(new LinkedList<>());

        customer.movieRentals()
                .parallelStream()
                .forEach(movieRental -> populateMovieRentalDebtRecords(movieRental, totalDebtAmountOwed, totalFrequentPoints, movieRentalDebtRecords));

        movieRentalDebtRecords.sort(Comparator.comparing(MovieRentalDebtRecord::debtOwedForTitle).reversed());
        return new CustomerRentalSummary(totalDebtAmountOwed, totalFrequentPoints, movieRentalDebtRecords);
    }

    public static void populateMovieRentalDebtRecords(MovieRental movieRental, AtomicDouble totalDebtAmountOwed, AtomicInteger totalFrequentPoints, List<MovieRentalDebtRecord> movieRentalDebtRecords) {
        Double debtOwedForTitle = MovieRentalUtils.calculateDebtOwedForMovieRental(movieRental);
        totalDebtAmountOwed.updateAndGet(v -> v + debtOwedForTitle);
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
