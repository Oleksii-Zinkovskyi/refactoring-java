package com.etraveli.practice.helper;

import com.etraveli.practice.dto.MovieRentalInfoOutput;
import com.etraveli.practice.dto.MovieRentalDebtRecord;
import com.etraveli.practice.formatter.RentalInfoFormatter;
import com.etraveli.practice.formatter.impl.StringRentalInfoFormatter;
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
        MovieRentalInfoOutput output = MovieRentalInfoOutput.builder()
                .customerName("C. U. Stomer")
                .movieRentalDebtRecords(List.of(new MovieRentalDebtRecord("You've Got Mail", 3.5), new MovieRentalDebtRecord("Matrix", 2.0)))
                .totalDebtAmountOwed(5.5)
                .totalFrequentPoints(2)
                .build();
        
        String result = rentalInfoFormatter.formatOutput(output);
        assertThat(result).isEqualTo(expected);
    }

    @Test
    public void givenValidCustomerAndSecondSetOfRentals_whenFormatOutput_thenReturnStringOutput() {
        String expected = "Rental Record for C. U. Stomer\n\tFast & Furious X\t3.0\n\tCars\t1.5\nAmount owed is 4.5\nYou earned 2 frequent points\n";
        MovieRentalInfoOutput output = MovieRentalInfoOutput.builder()
                .customerName("C. U. Stomer")
                .movieRentalDebtRecords(List.of(new MovieRentalDebtRecord("Fast & Furious X", 3.0), new MovieRentalDebtRecord("Cars", 1.5)))
                .totalDebtAmountOwed(4.5)
                .totalFrequentPoints(2)
                .build();

        String result = rentalInfoFormatter.formatOutput(output);
        assertThat(result).isEqualTo(expected);
    }

    //Not knowing the business logic deeper (and not feeling like this justifies emailing you), I assume that a customer can rent multiple copies of the same movie
    @Test
    public void givenValidCustomerWithDuplicateRentals_whenFormatOutput_thenReturnStringOutput() {
        String expected = "Rental Record for C. U. Stomer\n\tYou've Got Mail\t9.5\n\tYou've Got Mail\t3.5\n\tCars\t1.5\nAmount owed is 14.5\nYou earned 3 frequent points\n";
        MovieRentalInfoOutput output = MovieRentalInfoOutput.builder()
                .customerName("C. U. Stomer")
                .movieRentalDebtRecords(List.of(new MovieRentalDebtRecord("You've Got Mail", 9.5), new MovieRentalDebtRecord("You've Got Mail", 3.5), new MovieRentalDebtRecord("Cars", 1.5)))
                .totalDebtAmountOwed(14.5)
                .totalFrequentPoints(3)
                .build();

        String result = rentalInfoFormatter.formatOutput(output);
        assertThat(result).isEqualTo(expected);
    }

    @Test
    public void givenValidCustomerWithZeroDaysRental_whenFormatOutput_thenReturnStringOutput() {
        String expected = "Rental Record for C. U. Stomer\n\tYou've Got Mail\t2.0\nAmount owed is 2.0\nYou earned 1 frequent points\n";
        MovieRentalInfoOutput output = MovieRentalInfoOutput.builder()
                .customerName("C. U. Stomer")
                .movieRentalDebtRecords(List.of(new MovieRentalDebtRecord("You've Got Mail", 2.0)))
                .totalDebtAmountOwed(2.0)
                .totalFrequentPoints(1)
                .build();

        String result = rentalInfoFormatter.formatOutput(output);
        assertThat(result).isEqualTo(expected);
    }

    @Test
    public void givenCustomerWithNoRentals_whenFormatOutput_thenReturnStringOutput() {
        String expected = "Rental Record for C. U. Stomer\nAmount owed is 0.0\nYou earned 0 frequent points\n";
        MovieRentalInfoOutput output = MovieRentalInfoOutput.builder()
                .customerName("C. U. Stomer")
                .movieRentalDebtRecords(Collections.emptyList())
                .totalDebtAmountOwed(0.0)
                .totalFrequentPoints(0)
                .build();

        String result = rentalInfoFormatter.formatOutput(output);
        assertThat(result).isEqualTo(expected);
    }

    @Test
    public void givenCustomerWithNoNameOrRentals_whenFormatOutput_thenReturnStringOutput() {
        String expected = "Rental Record for \nAmount owed is 0.0\nYou earned 0 frequent points\n";
        MovieRentalInfoOutput output = MovieRentalInfoOutput.builder()
                .customerName("")
                .movieRentalDebtRecords(Collections.emptyList())
                .totalDebtAmountOwed(0.0)
                .totalFrequentPoints(0)
                .build();

        String result = rentalInfoFormatter.formatOutput(output);
        assertThat(result).isEqualTo(expected);
    }

}
