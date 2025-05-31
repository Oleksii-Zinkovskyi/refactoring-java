package com.etraveli.practice.supplier.impl;

import com.etraveli.practice.dto.Customer;
import com.etraveli.practice.dto.MovieRental;
import com.etraveli.practice.supplier.GenericSupplier;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class DefaultCustomerSupplier implements GenericSupplier<Customer> {

    @Override
    public Customer get() {
        return new Customer("C. U. Stomer", Arrays.asList(
                new MovieRental("F001", 3),
                new MovieRental("F002", 1)
        ));
    }
}
