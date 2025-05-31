package com.etraveli.practice.helper;

import com.etraveli.practice.dto.RentalInfoOutput;
import com.etraveli.practice.dto.RentalOwedAmount;
import com.etraveli.practice.helper.impl.StringRentalInfoFormatter;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class StringRentalInfoFormatterTest {

    private static RentalInfoFormatter rentalInfoFormatter;
    
    @BeforeAll
    public static void setUp() {
        rentalInfoFormatter = new StringRentalInfoFormatter();
    }
    
    @Test
    public void givenValidCustomerAndFirstSetOfRentals_whenFormatOutput_thenReturnStringOutput() {
        String expected = "Rental Record for C. U. Stomer\n\tYou've Got Mail\t3.5\n\tMatrix\t2.0\nAmount owed is 5.5\nYou earned 2 frequent points\n";
        RentalInfoOutput output = RentalInfoOutput.builder()
                .customerName("C. U. Stomer")
                .rentalDebtRecords(List.of(new RentalOwedAmount("You've Got Mail", 3.5), new RentalOwedAmount("Matrix", 2.0)))
                .totalOwedAmount(5.5)
                .totalPoints(2)
                .build();
        
        String result = rentalInfoFormatter.formatOutput(output);
        assertThat(result).isEqualTo(expected);
    }

    @Test
    public void givenValidCustomerAndSecondSetOfRentals_whenFormatOutput_thenReturnStringOutput() {
        String expected = "Rental Record for C. U. Stomer\n\tFast & Furious X\t3.0\n\tCars\t1.5\nAmount owed is 4.5\nYou earned 2 frequent points\n";
        RentalInfoOutput output = RentalInfoOutput.builder()
                .customerName("C. U. Stomer")
                .rentalDebtRecords(List.of(new RentalOwedAmount("Fast & Furious X", 3.0), new RentalOwedAmount("Cars", 1.5)))
                .totalOwedAmount(4.5)
                .totalPoints(2)
                .build();

        String result = rentalInfoFormatter.formatOutput(output);
        assertThat(result).isEqualTo(expected);
    }

    //Technically, a customer can rent two copies of the same movie, as unlikely as it may be
    @Test
    public void givenValidCustomerWithDuplicateRentals_whenFormatOutput_thenReturnStringOutput() {
        String expected = "Rental Record for C. U. Stomer\n\tYou've Got Mail\t9.5\n\tYou've Got Mail\t3.5\n\tCars\t1.5\nAmount owed is 14.5\nYou earned 3 frequent points\n";
        RentalInfoOutput output = RentalInfoOutput.builder()
                .customerName("C. U. Stomer")
                .rentalDebtRecords(List.of(new RentalOwedAmount("You've Got Mail", 9.5), new RentalOwedAmount("You've Got Mail", 3.5), new RentalOwedAmount("Cars", 1.5)))
                .totalOwedAmount(14.5)
                .totalPoints(3)
                .build();

        String result = rentalInfoFormatter.formatOutput(output);
        assertThat(result).isEqualTo(expected);
    }

    @Test
    public void givenValidCustomerWithZeroDaysRental_whenFormatOutput_thenReturnStringOutput() {
        String expected = "Rental Record for C. U. Stomer\n\tYou've Got Mail\t2.0\nAmount owed is 2.0\nYou earned 1 frequent points\n";
        RentalInfoOutput output = RentalInfoOutput.builder()
                .customerName("C. U. Stomer")
                .rentalDebtRecords(List.of(new RentalOwedAmount("You've Got Mail", 2.0)))
                .totalOwedAmount(2.0)
                .totalPoints(1)
                .build();

        String result = rentalInfoFormatter.formatOutput(output);
        assertThat(result).isEqualTo(expected);
    }

    @Test
    public void givenCustomerWithNoRentals_whenFormatOutput_thenReturnStringOutput() {
        String expected = "Rental Record for C. U. Stomer\nAmount owed is 0.0\nYou earned 0 frequent points\n";
        RentalInfoOutput output = RentalInfoOutput.builder()
                .customerName("C. U. Stomer")
                .rentalDebtRecords(Collections.emptyList())
                .totalOwedAmount(0.0)
                .totalPoints(0)
                .build();

        String result = rentalInfoFormatter.formatOutput(output);
        assertThat(result).isEqualTo(expected);
    }

    @Test
    public void givenCustomerWithNoNameOrRentals_whenFormatOutput_thenReturnStringOutput() {
        String expected = "Rental Record for \nAmount owed is 0.0\nYou earned 0 frequent points\n";
        RentalInfoOutput output = RentalInfoOutput.builder()
                .customerName("")
                .rentalDebtRecords(Collections.emptyList())
                .totalOwedAmount(0.0)
                .totalPoints(0)
                .build();

        String result = rentalInfoFormatter.formatOutput(output);
        assertThat(result).isEqualTo(expected);
    }

}
