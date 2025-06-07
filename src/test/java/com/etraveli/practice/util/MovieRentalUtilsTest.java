package com.etraveli.practice.util;

import com.etraveli.practice.dto.*;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class MovieRentalUtilsTest {

    @Test
    public void givenValidRegularMovieRental_whenCalculateDebtOwedForMovieRental_thenReturnsCorrectAmount() {
        MovieRental movieRental = new MovieRental("F001", 3);
        Movie movie = new Movie("You've Got Mail", MovieCode.REGULAR);

        Double debt = MovieRentalUtils.calculateDebtOwedForMovieRental(movieRental, movie);

        assertThat(debt).isEqualTo(3.5);
    }

    @Test
    public void givenValidNewMovieRental_whenCalculateDebtOwedForMovieRental_thenReturnsCorrectAmount() {
        MovieRental movieRental = new MovieRental("F002", 4);
        Movie movie = new Movie("Matrix", MovieCode.REGULAR);

        Double debt = MovieRentalUtils.calculateDebtOwedForMovieRental(movieRental, movie);

        assertThat(debt).isEqualTo(5.0);
    }

    @Test
    public void givenValidChildrensMovieRental_whenCalculateDebtOwedForMovieRental_thenReturnsCorrectAmount() {
        MovieRental movieRental = new MovieRental("F003", 5);
        Movie movie = new Movie("Cars", MovieCode.CHILDREN);

        Double debt = MovieRentalUtils.calculateDebtOwedForMovieRental(movieRental, movie);

        assertThat(debt).isEqualTo(4.5);
    }

    @Test
    public void givenValidMovieRental_whenCalculateFrequentPoints_thenReturnsCorrectAmount() {
        MovieRental movieRental = new MovieRental("F001", 3);
        Movie movie = new Movie("You've Got Mail", MovieCode.REGULAR);

        Integer points = MovieRentalUtils.calculateFrequentPoints(movieRental, movie);

        assertThat(points).isEqualTo(1);
    }

    @Test
    public void givenValidNewLongMovieRental_whenCalculateFrequentPoints_thenReturnsCorrectAmount() {
        MovieRental movieRental = new MovieRental("F004", 5);
        Movie movie = new Movie("Fast & Furious X", MovieCode.NEW);

        Integer points = MovieRentalUtils.calculateFrequentPoints(movieRental, movie);

        assertThat(points).isEqualTo(2);
    }

    @Test
    public void givenValidNewShortMovieRental_whenCalculateFrequentPoints_thenReturnsCorrectAmount() {
        MovieRental movieRental = new MovieRental("F004", 1);
        Movie movie = new Movie("Fast & Furious X", MovieCode.NEW);

        Integer points = MovieRentalUtils.calculateFrequentPoints(movieRental, movie);

        assertThat(points).isEqualTo(1);
    }

    @Test
    public void givenTwoCustomerRentalSummaries_whenMergeCustomerRentalSummaries_thenReturnsMergedSummary() {
        MovieRentalDebtRecord movieRecordA = new MovieRentalDebtRecord("Movie A", 5.0);
        MovieRentalDebtRecord movieRecordB = new MovieRentalDebtRecord("Movie B", 7.5);
        CustomerRentalSummary summary1 = new CustomerRentalSummary(10.0, 2, List.of(movieRecordA));
        CustomerRentalSummary summary2 = new CustomerRentalSummary(15.0, 3, List.of(movieRecordB));

        CustomerRentalSummary mergedSummary = MovieRentalUtils.mergeCustomerRentalSummaries().apply(summary1, summary2);

        assertThat(mergedSummary.totalDebtAmountOwed()).isEqualTo(25.0);
        assertThat(mergedSummary.totalFrequentPoints()).isEqualTo(5);
        assertThat(mergedSummary.movieRentalDebtRecords()).hasSize(2);
        assertThat(mergedSummary.movieRentalDebtRecords().get(0)).isEqualTo(movieRecordA);
        assertThat(mergedSummary.movieRentalDebtRecords().get(1)).isEqualTo(movieRecordB);
    }

    @Test
    public void givenUnsortedCustomerRentalSummary_whenSortCustomerRentalSummary_thenReturnsSortedSummary() {
        MovieRentalDebtRecord movieRecordA = new MovieRentalDebtRecord("Movie A", 5.0);
        MovieRentalDebtRecord movieRecordB = new MovieRentalDebtRecord("Movie B", 7.5);
        MovieRentalDebtRecord movieRecordC = new MovieRentalDebtRecord("Movie C", 3.0);
        CustomerRentalSummary unsortedSummary = new CustomerRentalSummary(15.5, 4, List.of(movieRecordA, movieRecordB, movieRecordC));

        CustomerRentalSummary sortedSummary = MovieRentalUtils.sortCustomerRentalSummary(unsortedSummary);

        assertThat(sortedSummary.movieRentalDebtRecords()).hasSize(3);
        assertThat(sortedSummary.movieRentalDebtRecords().get(0)).isEqualTo(movieRecordB);
        assertThat(sortedSummary.movieRentalDebtRecords().get(1)).isEqualTo(movieRecordA);
        assertThat(sortedSummary.movieRentalDebtRecords().get(2)).isEqualTo(movieRecordC);
    }

}
