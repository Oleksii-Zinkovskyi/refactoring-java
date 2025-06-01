package com.etraveli.practice.supplier;

import com.etraveli.practice.dto.Customer;
import com.etraveli.practice.dto.MovieRental;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.function.Supplier;

@Component
public class DefaultCustomerSupplier implements Supplier<Customer> {

    @Override
    public Customer get() {
        return new Customer("C. U. Stomer", Arrays.asList(
                new MovieRental("F001", 3),
                new MovieRental("F002", 1)
        ));
    }

}
