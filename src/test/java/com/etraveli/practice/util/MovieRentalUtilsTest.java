package com.etraveli.practice.util;

import com.etraveli.practice.dto.MovieRental;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class MovieRentalUtilsTest {

    @Test
    public void givenValidRegularMovieRental_whenCalculateDebtOwedForMovieRental_thenReturnsCorrectAmount() {
        MovieRental movieRental = new MovieRental("F001", 3);

        Double debt = MovieRentalUtils.calculateDebtOwedForMovieRental(movieRental);

        assertThat(debt).isEqualTo(3.5);
    }

    @Test
    public void givenValidNewMovieRental_whenCalculateDebtOwedForMovieRental_thenReturnsCorrectAmount() {
        MovieRental movieRental = new MovieRental("F002", 4);

        Double debt = MovieRentalUtils.calculateDebtOwedForMovieRental(movieRental);

        assertThat(debt).isEqualTo(5.0);
    }

    @Test
    public void givenValidChildrensMovieRental_whenCalculateDebtOwedForMovieRental_thenReturnsCorrectAmount() {
        MovieRental movieRental = new MovieRental("F003", 5);

        Double debt = MovieRentalUtils.calculateDebtOwedForMovieRental(movieRental);

        assertThat(debt).isEqualTo(4.5);
    }

    @Test
    public void givenValidMovieRental_whenCalculateFrequentPoints_thenReturnsCorrectAmount() {
        MovieRental movieRental = new MovieRental("F001", 3);

        Integer points = MovieRentalUtils.calculateFrequentPoints(movieRental);

        assertThat(points).isEqualTo(1);
    }

    @Test
    public void givenValidNewLongMovieRental_whenCalculateFrequentPoints_thenReturnsCorrectAmount() {
        MovieRental movieRental = new MovieRental("F004", 5);

        Integer points = MovieRentalUtils.calculateFrequentPoints(movieRental);

        assertThat(points).isEqualTo(2);
    }

    @Test
    public void givenValidNewShortMovieRental_whenCalculateFrequentPoints_thenReturnsCorrectAmount() {
        MovieRental movieRental = new MovieRental("F004", 1);

        Integer points = MovieRentalUtils.calculateFrequentPoints(movieRental);

        assertThat(points).isEqualTo(1);
    }

}
