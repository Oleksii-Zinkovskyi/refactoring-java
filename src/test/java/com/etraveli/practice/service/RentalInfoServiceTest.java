package com.etraveli.practice.service;

import com.etraveli.practice.dto.Customer;
import com.etraveli.practice.dto.MovieRental;
import com.etraveli.practice.dto.RentalInfoOutput;
import com.etraveli.practice.dto.RentalOwedAmount;
import com.etraveli.practice.helper.RentalInfoFormatter;
import com.etraveli.practice.service.impl.RentalInfoServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

//TODO: Add stress tests with many records per person, verify concurrent execution ordering
public class RentalInfoServiceTest {

    @Mock
    private RentalInfoFormatter rentalInfoFormatter;

    @InjectMocks
    private RentalInfoServiceImpl rentalInfoService;

    private AutoCloseable closeable;

    @BeforeEach
    public void setUp() {
        closeable = MockitoAnnotations.openMocks(this);

        Mockito.when(rentalInfoFormatter.formatOutput(Mockito.any())).thenReturn("Mocked Output");
    }

    @AfterEach
    public void tearDown() throws Exception {
        if (closeable != null) {
            closeable.close();
        }
    }

    /* P.S. We could declare private methods of the RentalInfoService as @VisibleForTesting and make them protected to allow for UnitTesting, but I think it isn't worth it
     * So this is where tests for processOwedAmountForRecord and processFrequentEnterPoints would go if we did that. It is bad practice, however, so I will refactor it.*/

    @Test
    public void givenValidCustomerAndFirstSetOfRentals_whenGenerateStatement_thenReturnRentalInfoOutput() {
        Customer customer = new Customer("C. U. Stomer", Arrays.asList(new MovieRental("F001", 3), new MovieRental("F002", 1)));

        rentalInfoService.generateStatementForCustomer(customer);

        verify(rentalInfoFormatter, times(1)).formatOutput(
                RentalInfoOutput.builder()
                        .customerName("C. U. Stomer")
                        .rentalDebtRecords(List.of(new RentalOwedAmount("You've Got Mail", 3.5), new RentalOwedAmount("Matrix", 2.0)))
                        .totalOwedAmount(5.5)
                        .totalPoints(2)
                        .build());
    }

    @Test
    public void givenValidCustomerAndSecondSetOfRentals_whenGenerateStatement_thenReturnRentalInfoOutput() {
        Customer customer = new Customer("C. U. Stomer", Arrays.asList(new MovieRental("F003", 3), new MovieRental("F004", 1)));

        rentalInfoService.generateStatementForCustomer(customer);

        verify(rentalInfoFormatter, times(1)).formatOutput(
                RentalInfoOutput.builder()
                        .customerName("C. U. Stomer")
                        .rentalDebtRecords(List.of(new RentalOwedAmount("Fast & Furious X", 3.0), new RentalOwedAmount("Cars", 1.5)))
                        .totalOwedAmount(4.5)
                        .totalPoints(2)
                        .build());
    }

    //Technically, a customer can rent two copies of the same movie, as unlikely as it may be
    @Test
    public void givenValidCustomerWithDuplicateRentals_whenGenerateStatement_thenReturnRentalInfoOutput() {
        Customer customer = new Customer("C. U. Stomer", Arrays.asList(new MovieRental("F001", 3), new MovieRental("F001", 7), new MovieRental("F003", 1)));

        rentalInfoService.generateStatementForCustomer(customer);

        verify(rentalInfoFormatter, times(1)).formatOutput(
                RentalInfoOutput.builder()
                        .customerName("C. U. Stomer")
                        .rentalDebtRecords(List.of(new RentalOwedAmount("You've Got Mail", 9.5), new RentalOwedAmount("You've Got Mail", 3.5), new RentalOwedAmount("Cars", 1.5)))
                        .totalOwedAmount(14.5)
                        .totalPoints(3)
                        .build());
    }

    @Test
    public void givenValidCustomerWithInvalidRental_whenGenerateStatement_thenThrowException() {
        Customer customer = new Customer("C. U. Stomer", List.of(new MovieRental("WRONG", 3)));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                rentalInfoService.generateStatementForCustomer(customer));

        assertThat(exception.getMessage()).isEqualTo("Invalid movie ID: WRONG");
    }

    @Test
    public void givenValidCustomerWithZeroDaysRental_whenGenerateStatement_thenReturnRentalInfoOutput() {
        Customer customer = new Customer("C. U. Stomer", List.of(new MovieRental("F001", 0)));

        rentalInfoService.generateStatementForCustomer(customer);

        verify(rentalInfoFormatter, times(1)).formatOutput(
                RentalInfoOutput.builder()
                        .customerName("C. U. Stomer")
                        .rentalDebtRecords(List.of(new RentalOwedAmount("You've Got Mail", 2.0)))
                        .totalOwedAmount(2.0)
                        .totalPoints(1)
                        .build());
    }

    @Test
    public void givenValidCustomerWithMinDaysRental_whenGenerateStatement_thenThrowException() {
        Customer customer = new Customer("C. U. Stomer", List.of(new MovieRental("F001", Integer.MIN_VALUE)));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                rentalInfoService.generateStatementForCustomer(customer));

        assertThat(exception.getMessage()).isEqualTo("Invalid rental record for F001, days value outside of bounds: -2147483648");
    }

    @Test
    public void givenValidCustomerWithMaxDaysRental_whenGenerateStatement_thenThrowException() {
        Customer customer = new Customer("C. U. Stomer", List.of(new MovieRental("F001", Integer.MAX_VALUE)));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                rentalInfoService.generateStatementForCustomer(customer));

        assertThat(exception.getMessage()).isEqualTo("Invalid rental record for F001, days value outside of bounds: 2147483647");
    }

    @Test
    public void givenCustomerWithNoRentals_whenGenerateStatement_thenReturnRentalInfoOutput() {
        Customer customer = new Customer("C. U. Stomer", Collections.emptyList());

        rentalInfoService.generateStatementForCustomer(customer);

        verify(rentalInfoFormatter, times(1)).formatOutput(
                RentalInfoOutput.builder()
                        .customerName("C. U. Stomer")
                        .rentalDebtRecords(Collections.emptyList())
                        .totalOwedAmount(0.0)
                        .totalPoints(0)
                        .build());
    }

    @Test
    public void givenCustomerWithNoNameOrRentals_whenGenerateStatement_thenReturnRentalInfoOutput() {
        Customer customer = new Customer("", Collections.emptyList());

        rentalInfoService.generateStatementForCustomer(customer);

        verify(rentalInfoFormatter, times(1)).formatOutput(
                RentalInfoOutput.builder()
                        .customerName("")
                        .rentalDebtRecords(Collections.emptyList())
                        .totalOwedAmount(0.0)
                        .totalPoints(0)
                        .build());
    }

    @Test
    public void givenNoCustomer_whenGenerateStatement_thenThrowException() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                rentalInfoService.generateStatementForCustomer(null));

        assertThat(exception.getMessage()).isEqualTo("Customer cannot be null");
    }

}
