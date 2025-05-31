package com.etraveli.practice.service.impl;

import com.etraveli.practice.dto.Customer;
import com.etraveli.practice.dto.MovieEnum;
import com.etraveli.practice.dto.MovieRentalInfoOutput;
import com.etraveli.practice.dto.MovieRentalDebtRecord;
import com.etraveli.practice.formatter.RentalInfoFormatter;
import com.etraveli.practice.helper.RentalInfoHelper;
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
        RentalInfoHelper.validateCustomer(customer);

        AtomicDouble totalDebtAmountOwed = new AtomicDouble(0);
        AtomicInteger totalFrequentPoints = new AtomicInteger();
        List<MovieRentalDebtRecord> movieRentalDebtRecords = Collections.synchronizedList(new LinkedList<>());

        customer.movieRentals()
                .parallelStream()
                .forEach(movieRental -> {
                    double debtOwedForTitle = RentalInfoHelper.calculateDebtOwedForMovieRental(movieRental);
                    totalDebtAmountOwed.updateAndGet(v -> v + debtOwedForTitle);
                    RentalInfoHelper.accumulateFrequentPoints(movieRental, totalFrequentPoints);
                    movieRentalDebtRecords.add(new MovieRentalDebtRecord(MovieEnum.valueOf(movieRental.movieId()).getTitle(), debtOwedForTitle));
                });

        //Sorting to present the largest items contributing to the price first
        movieRentalDebtRecords.sort(Comparator.comparing(MovieRentalDebtRecord::debtOwedForTitle).reversed());

        return rentalInfoFormatter.formatOutput(
                MovieRentalInfoOutput.builder()
                        .customerName(customer.name())
                        .movieRentalDebtRecords(movieRentalDebtRecords)
                        .totalDebtAmountOwed(totalDebtAmountOwed.get())
                        .totalFrequentPoints(totalFrequentPoints.get())
                        .build());
    }

}
