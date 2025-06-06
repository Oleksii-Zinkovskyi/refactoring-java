package com.etraveli.practice.util;

import com.etraveli.practice.dto.Movie;
import com.etraveli.practice.dto.MovieCode;
import com.etraveli.practice.dto.MovieRental;
import org.junit.jupiter.api.Test;

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

    //P.S. There could be more tests here, but both the comparator and sort methods are simple enough that they don't require extensive testing.

}
