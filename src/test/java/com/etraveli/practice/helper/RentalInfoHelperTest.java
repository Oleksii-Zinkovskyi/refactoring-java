package com.etraveli.practice.helper;

import com.etraveli.practice.dto.Customer;
import com.etraveli.practice.dto.MovieRental;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class RentalInfoHelperTest {

    @Test
    public void givenValidCustomer_whenValidateCustomer_thenDoesNothing() {
        Customer customer = new Customer("John Doe", Collections.emptyList());

        assertDoesNotThrow(() -> RentalInfoHelper.validateCustomer(customer));
    }

    @Test
    public void givenNullCustomer_whenValidateCustomer_thenThrowsException() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                RentalInfoHelper.validateCustomer(null));

        assertThat(exception.getMessage()).isEqualTo("Customer cannot be null");
    }

    @Test
    public void givenValidMovieRecord_whenValidateMovieRecord_thenDoesNothing() {
        MovieRental movieRental = new MovieRental("F001", 3);

        assertDoesNotThrow(() -> {
            RentalInfoHelper.validateMovieRecord(movieRental);
        });
    }

    @Test
    public void givenMovieRecordWithInvalidId_whenValidateMovieRecord_thenThrowsException() {
        MovieRental movieRental = new MovieRental("InvalidID", 3);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                RentalInfoHelper.validateMovieRecord(movieRental));

        assertThat(exception.getMessage()).isEqualTo("Invalid movie ID: InvalidID");
    }

    @Test
    public void givenMovieRentalWithNegativeDaysRented_whenValidateMovieRecord_thenThrowsException() {
        MovieRental movieRental = new MovieRental("F001", -1);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                RentalInfoHelper.validateMovieRecord(movieRental));

        assertThat(exception.getMessage()).isEqualTo("Invalid rental record for F001, daysRented value outside of bounds: -1");
    }

    @Test
    public void givenMovieRentalWithDaysRentedExceedingLimit_whenValidateMovieRecord_thenThrowsException() {
        MovieRental movieRental = new MovieRental("F001", 36525); // > 100 years in days

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                RentalInfoHelper.validateMovieRecord(movieRental));

        assertThat(exception.getMessage()).isEqualTo("Invalid rental record for F001, daysRented value outside of bounds: 36525");
    }

    @Test
    public void givenValidRegularMovieRental_whenCalculateDebtOwedForMovieRental_thenReturnsCorrectAmount() {
        MovieRental movieRental = new MovieRental("F001", 3);

        double debt = RentalInfoHelper.calculateDebtOwedForMovieRental(movieRental);

        assertThat(debt).isEqualTo(3.5);
    }

    @Test
    public void givenValidNewMovieRental_whenCalculateDebtOwedForMovieRental_thenReturnsCorrectAmount() {
        MovieRental movieRental = new MovieRental("F002", 4);

        double debt = RentalInfoHelper.calculateDebtOwedForMovieRental(movieRental);

        assertThat(debt).isEqualTo(5.0);
    }

    @Test
    public void givenValidChildrensMovieRental_whenCalculateDebtOwedForMovieRental_thenReturnsCorrectAmount() {
        MovieRental movieRental = new MovieRental("F003", 5);

        double debt = RentalInfoHelper.calculateDebtOwedForMovieRental(movieRental);

        assertThat(debt).isEqualTo(4.5);
    }

    @Test
    public void givenValidMovieRental_whenAccumulateFrequentPoints_thenIncrementsPoints() {
        MovieRental movieRental = new MovieRental("F001", 3);
        AtomicInteger totalFrequentPoints = new AtomicInteger(0);

        RentalInfoHelper.accumulateFrequentPoints(movieRental, totalFrequentPoints);

        assertThat(totalFrequentPoints.get()).isEqualTo(1);
    }

    @Test
    public void givenValidNewLongMovieRental_whenAccumulateFrequentPoints_thenIncrementsPoints() {
        MovieRental movieRental = new MovieRental("F004", 5);
        AtomicInteger totalFrequentPoints = new AtomicInteger(0);

        RentalInfoHelper.accumulateFrequentPoints(movieRental, totalFrequentPoints);

        assertThat(totalFrequentPoints.get()).isEqualTo(2);
    }

    @Test
    public void givenValidNewShortMovieRental_whenAccumulateFrequentPoints_thenIncrementsPoints() {
        MovieRental movieRental = new MovieRental("F004", 1);
        AtomicInteger totalFrequentPoints = new AtomicInteger(0);

        RentalInfoHelper.accumulateFrequentPoints(movieRental, totalFrequentPoints);

        assertThat(totalFrequentPoints.get()).isEqualTo(1);
    }

}
