package com.etraveli.practice.util;

import com.etraveli.practice.dto.Customer;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CustomerUtilsTest {

    @Test
    public void givenValidCustomer_whenPerformCustomerValidation_thenDoesNothing() {
        Customer customer = new Customer("John Doe", Collections.emptyList());

        assertDoesNotThrow(() -> CustomerUtils.performCustomerValidation(customer));
    }

    @Test
    public void givenNullCustomer_whenPerformCustomerValidation_thenThrowsException() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                CustomerUtils.performCustomerValidation(null));

        assertThat(exception.getMessage()).isEqualTo("Customer cannot be null");
    }

}
