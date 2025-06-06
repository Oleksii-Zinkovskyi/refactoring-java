package com.etraveli.practice.validator;

import com.etraveli.practice.dto.Customer;
import com.etraveli.practice.dto.ValidationResult;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

public class CustomerValidatorTest {

    @Test
    public void givenValidCustomer_whenValidateCustomer_thenReturnsSuccess() {
        Customer customer = new Customer("John Doe", Collections.emptyList());

        ValidationResult<Customer> result = CustomerValidator.validateCustomer(customer);

        assertThat(result.valid()).isTrue();
        assertThat(result.value()).isEqualTo(customer);
    }

    @Test
    public void givenNullCustomer_whenValidateCustomer_thenReturnsFailure() {
        ValidationResult<Customer> result = CustomerValidator.validateCustomer(null);

        assertThat(result.valid()).isFalse();
        assertThat(result.errorMessage()).isEqualTo("Customer cannot be null");
    }

}
