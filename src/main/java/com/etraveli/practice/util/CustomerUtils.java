package com.etraveli.practice.util;

import com.etraveli.practice.dto.Customer;
import com.etraveli.practice.dto.ValidationResult;
import com.etraveli.practice.validator.CustomerValidator;

public class CustomerUtils {

    public static void performCustomerValidation(Customer customer) {
        ValidationResult<Customer> customerValidation = CustomerValidator.validateCustomer(customer);
        if (!customerValidation.valid()) {
            //P.S. We could also be returning detailed messages in these, but I think an exception works better for this case
            throw new IllegalArgumentException(customerValidation.errorMessage());
        }
    }

}
