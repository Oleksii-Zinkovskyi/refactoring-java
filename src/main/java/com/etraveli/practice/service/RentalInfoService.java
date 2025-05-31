package com.etraveli.practice.service;

import com.etraveli.practice.dto.Customer;

public interface RentalInfoService {

    String generateStatementForCustomer(Customer customer);

}
