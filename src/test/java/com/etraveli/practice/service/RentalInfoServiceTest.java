package com.etraveli.practice.service;

import com.etraveli.practice.dto.Customer;
import com.etraveli.practice.dto.MovieRental;
import com.etraveli.practice.dto.MovieRentalInfoOutput;
import com.etraveli.practice.dto.MovieRentalDebtRecord;
import com.etraveli.practice.formatter.RentalInfoFormatter;
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

/* P.S. Now that helper methods are exposed and tested separately, many of these tests are somewhat of an overkill*/
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

    @Test
    public void givenValidCustomerAndFirstSetOfRentals_whenGenerateStatement_thenReturnRentalInfoOutput() {
        Customer customer = new Customer("C. U. Stomer", Arrays.asList(new MovieRental("F001", 3), new MovieRental("F002", 1)));

        rentalInfoService.generateStatementForCustomer(customer);

        verify(rentalInfoFormatter, times(1)).formatOutput(
                MovieRentalInfoOutput.builder()
                        .customerName("C. U. Stomer")
                        .movieRentalDebtRecords(List.of(new MovieRentalDebtRecord("You've Got Mail", 3.5), new MovieRentalDebtRecord("Matrix", 2.0)))
                        .totalDebtAmountOwed(5.5)
                        .totalFrequentPoints(2)
                        .build());
    }

    @Test
    public void givenValidCustomerAndSecondSetOfRentals_whenGenerateStatement_thenReturnRentalInfoOutput() {
        Customer customer = new Customer("C. U. Stomer", Arrays.asList(new MovieRental("F003", 3), new MovieRental("F004", 1)));

        rentalInfoService.generateStatementForCustomer(customer);

        verify(rentalInfoFormatter, times(1)).formatOutput(
                MovieRentalInfoOutput.builder()
                        .customerName("C. U. Stomer")
                        .movieRentalDebtRecords(List.of(new MovieRentalDebtRecord("Fast & Furious X", 3.0), new MovieRentalDebtRecord("Cars", 1.5)))
                        .totalDebtAmountOwed(4.5)
                        .totalFrequentPoints(2)
                        .build());
    }

    //Not knowing the business logic deeper (and not feeling like this justifies emailing you), I assume that a customer can rent multiple copies of the same movie
    @Test
    public void givenValidCustomerWithDuplicateRentals_whenGenerateStatement_thenReturnRentalInfoOutput() {
        Customer customer = new Customer("C. U. Stomer", Arrays.asList(new MovieRental("F001", 3), new MovieRental("F001", 7), new MovieRental("F003", 1)));

        rentalInfoService.generateStatementForCustomer(customer);

        verify(rentalInfoFormatter, times(1)).formatOutput(
                MovieRentalInfoOutput.builder()
                        .customerName("C. U. Stomer")
                        .movieRentalDebtRecords(List.of(new MovieRentalDebtRecord("You've Got Mail", 9.5), new MovieRentalDebtRecord("You've Got Mail", 3.5), new MovieRentalDebtRecord("Cars", 1.5)))
                        .totalDebtAmountOwed(14.5)
                        .totalFrequentPoints(3)
                        .build());
    }

    @Test
    public void givenValidCustomerWithManyRentals_whenGenerateStatement_thenReturnRentalInfoOutput() {
        Customer customer = new Customer("C. U. Stomer", Arrays.asList(new MovieRental("F001", 3), new MovieRental("F001", 7), new MovieRental("F003", 1), new MovieRental("F003", 15), new MovieRental("F001", 7), new MovieRental("F003", 0), new MovieRental("F004", 70), new MovieRental("F003", 12), new MovieRental("F004", 1)));

        rentalInfoService.generateStatementForCustomer(customer);

        verify(rentalInfoFormatter, times(1)).formatOutput(
                MovieRentalInfoOutput.builder()
                        .customerName("C. U. Stomer")
                        .movieRentalDebtRecords(List.of(new MovieRentalDebtRecord("Fast & Furious X", 210.0),
                                new MovieRentalDebtRecord("Cars", 19.5), new MovieRentalDebtRecord("Cars", 15.0),
                                new MovieRentalDebtRecord("You've Got Mail", 9.5), new MovieRentalDebtRecord("You've Got Mail", 9.5),
                                new MovieRentalDebtRecord("You've Got Mail", 3.5), new MovieRentalDebtRecord("Fast & Furious X", 3.0),
                                new MovieRentalDebtRecord("Cars", 1.5), new MovieRentalDebtRecord("Cars", 1.5)))
                        .totalDebtAmountOwed(273.0)
                        .totalFrequentPoints(10)
                        .build());
    }

    @Test
    public void givenValidCustomerWithInvalidRental_whenGenerateStatement_thenThrowException() {
        Customer customer = new Customer("C. U. Stomer", List.of(new MovieRental("InvalidID", 3)));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                rentalInfoService.generateStatementForCustomer(customer));

        assertThat(exception.getMessage()).isEqualTo("Invalid movie ID: InvalidID");
    }

    @Test
    public void givenValidCustomerWithZeroDaysRental_whenGenerateStatement_thenReturnRentalInfoOutput() {
        Customer customer = new Customer("C. U. Stomer", List.of(new MovieRental("F001", 0)));

        rentalInfoService.generateStatementForCustomer(customer);

        verify(rentalInfoFormatter, times(1)).formatOutput(
                MovieRentalInfoOutput.builder()
                        .customerName("C. U. Stomer")
                        .movieRentalDebtRecords(List.of(new MovieRentalDebtRecord("You've Got Mail", 2.0)))
                        .totalDebtAmountOwed(2.0)
                        .totalFrequentPoints(1)
                        .build());
    }

    @Test
    public void givenValidCustomerWithMinDaysRental_whenGenerateStatement_thenThrowException() {
        Customer customer = new Customer("C. U. Stomer", List.of(new MovieRental("F001", Integer.MIN_VALUE)));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                rentalInfoService.generateStatementForCustomer(customer));

        assertThat(exception.getMessage()).isEqualTo("Invalid rental record for F001, daysRented value outside of bounds: -2147483648");
    }

    @Test
    public void givenValidCustomerWithMaxDaysRental_whenGenerateStatement_thenThrowException() {
        Customer customer = new Customer("C. U. Stomer", List.of(new MovieRental("F001", Integer.MAX_VALUE)));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                rentalInfoService.generateStatementForCustomer(customer));

        assertThat(exception.getMessage()).isEqualTo("Invalid rental record for F001, daysRented value outside of bounds: 2147483647");
    }

    @Test
    public void givenCustomerWithNoRentals_whenGenerateStatement_thenReturnRentalInfoOutput() {
        Customer customer = new Customer("C. U. Stomer", Collections.emptyList());

        rentalInfoService.generateStatementForCustomer(customer);

        verify(rentalInfoFormatter, times(1)).formatOutput(
                MovieRentalInfoOutput.builder()
                        .customerName("C. U. Stomer")
                        .movieRentalDebtRecords(Collections.emptyList())
                        .totalDebtAmountOwed(0.0)
                        .totalFrequentPoints(0)
                        .build());
    }

    @Test
    public void givenCustomerWithNoNameOrRentals_whenGenerateStatement_thenReturnRentalInfoOutput() {
        Customer customer = new Customer("", Collections.emptyList());

        rentalInfoService.generateStatementForCustomer(customer);

        verify(rentalInfoFormatter, times(1)).formatOutput(
                MovieRentalInfoOutput.builder()
                        .customerName("")
                        .movieRentalDebtRecords(Collections.emptyList())
                        .totalDebtAmountOwed(0.0)
                        .totalFrequentPoints(0)
                        .build());
    }

    @Test
    public void givenNoCustomer_whenGenerateStatement_thenThrowException() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                rentalInfoService.generateStatementForCustomer(null));

        assertThat(exception.getMessage()).isEqualTo("Customer cannot be null");
    }

}
