package com.etraveli.practice.validator;

import com.etraveli.practice.dto.Customer;
import com.etraveli.practice.dto.ValidationResult;

public class CustomerValidator {

    public static ValidationResult<Customer> validateCustomer(Customer customer) {
        //P.S. In real world applications, we would likely have more sophisticated validation logic
        if (customer == null) {
            return ValidationResult.failure("Customer cannot be null");
        } else {
            return ValidationResult.success(customer);
        }
    }

}
