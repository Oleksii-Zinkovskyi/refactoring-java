package com.etraveli.practice.helper;

import com.etraveli.practice.dto.Customer;
import com.etraveli.practice.dto.MovieRental;
import com.etraveli.practice.service.RentalInfoService;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class RentalInfoServiceTest {

    @Test
    public void givenValidCustomerAndRentals_whenGenerateStatement_thenReturnInfo() {
        String expected = "Rental Record for C. U. Stomer\n\tYou've Got Mail\t3.5\n\tMatrix\t2.0\nAmount owed is 5.5\nYou earned 2 frequent points\n";
        String result = new RentalInfoService().statement(new Customer("C. U. Stomer", Arrays.asList(new MovieRental("F001", 3), new MovieRental("F002", 1))));
        assertThat(result).isEqualTo(expected);
    }

    //Technically, a customer can rent two copies of the same movie, as unlikely as it may be
    @Test
    public void givenValidCustomerWithDuplicateRentals_whenGenerateStatement_thenReturnInfo() {
        String expected = "Rental Record for C. U. Stomer\n\tYou've Got Mail\t3.5\n\tYou've Got Mail\t9.5\n\tCars\t1.5\nAmount owed is 14.5\nYou earned 3 frequent points\n";
        String result = new RentalInfoService().statement(new Customer("C. U. Stomer", Arrays.asList(new MovieRental("F001", 3), new MovieRental("F001", 7), new MovieRental("F003", 1))));
        assertThat(result).isEqualTo(expected);
    }

    //TODO: Obviously throwing NPEs is not a good practice, I am merely documenting the current behavior
    @Test
    public void givenValidCustomerWithInvalidRental_whenGenerateStatement_thenThrowNPE() {
        assertThrows(NullPointerException.class, () ->
                new RentalInfoService().statement(new Customer("C. U. Stomer", List.of(new MovieRental("WRONG", 3)))));
    }

    @Test
    public void givenValidCustomerWithZeroDaysRental_whenGenerateStatement_thenReturnInfo() {
        String expected = "Rental Record for C. U. Stomer\n\tYou've Got Mail\t2.0\nAmount owed is 2.0\nYou earned 1 frequent points\n";
        String result = new RentalInfoService().statement(new Customer("C. U. Stomer", List.of(new MovieRental("F001", 0))));
        assertThat(result).isEqualTo(expected);
    }

    //TODO: This one is also odd, I am pretty sure we shouldn't allow negative days, but the original code does not handle it
    @Test
    public void givenValidCustomerWithMinDaysRental_whenGenerateStatement_thenReturnInfo() {
        String expected = "Rental Record for C. U. Stomer\n\tYou've Got Mail\t2.0\nAmount owed is 2.0\nYou earned 1 frequent points\n";
        String result = new RentalInfoService().statement(new Customer("C. U. Stomer", List.of(new MovieRental("F001", Integer.MIN_VALUE))));
        assertThat(result).isEqualTo(expected);
    }

    //TODO: So this isn't handled well either, we'll see if it can be somehow improved
    @Test
    public void givenValidCustomerWithMaxDaysRental_whenGenerateStatement_thenReturnInfo() {
        String expected = "Rental Record for C. U. Stomer\n\tYou've Got Mail\t3.2212254695E9\nAmount owed is 3.2212254695E9\nYou earned 1 frequent points\n";
        String result = new RentalInfoService().statement(new Customer("C. U. Stomer", List.of(new MovieRental("F001", Integer.MAX_VALUE))));
        assertThat(result).isEqualTo(expected);
    }

    @Test
    public void givenCustomerWithNoRentals_whenGenerateStatement_thenReturnInfo() {
        String expected = "Rental Record for C. U. Stomer\nAmount owed is 0.0\nYou earned 0 frequent points\n";
        String result = new RentalInfoService().statement(new Customer("C. U. Stomer", Collections.emptyList()));
        assertThat(result).isEqualTo(expected);
    }

    @Test
    public void givenCustomerWithNoNameOrRentals_whenGenerateStatement_thenReturnInfo() {
        String expected = "Rental Record for \nAmount owed is 0.0\nYou earned 0 frequent points\n";
        String result = new RentalInfoService().statement(new Customer("", Collections.emptyList()));
        assertThat(result).isEqualTo(expected);
    }

    //TODO: Obviously throwing NPEs is not a good practice, I am merely documenting the current behavior
    @Test
    public void givenNullCustomerWithNullRentals_whenGenerateStatement_thenThrowNPE() {
        assertThrows(NullPointerException.class, () ->
                new RentalInfoService().statement(new Customer(null, null)));
    }

    //TODO: Obviously throwing NPEs is not a good practice, I am merely documenting the current behavior
    @Test
    public void givenNoCustomer_whenGenerateStatement_thenThrowNPE() {
        assertThrows(NullPointerException.class, () ->
                new RentalInfoService().statement(null));
    }

}
