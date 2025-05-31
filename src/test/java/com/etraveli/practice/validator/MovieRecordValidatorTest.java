package com.etraveli.practice.validator;

import com.etraveli.practice.dto.MovieEnum;
import com.etraveli.practice.dto.MovieRental;
import com.etraveli.practice.dto.ValidationResult;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class MovieRecordValidatorTest {

    @Test
    public void givenValidMovieRecord_whenValidateMovieRecord_thenReturnsSuccess() {
        MovieRental movieRental = new MovieRental("F001", 3);
        MovieEnum movieEnum = MovieEnum.F001;

        ValidationResult<MovieEnum> result = MovieRecordValidator.validateMovieRecord(movieRental);

        assertThat(result.valid()).isTrue();
        assertThat(result.value()).isEqualTo(movieEnum);
    }

    @Test
    public void givenMovieRecordWithInvalidId_whenValidateMovieRecord_thenReturnsFailure() {
        MovieRental movieRental = new MovieRental("InvalidID", 3);

        ValidationResult<MovieEnum> result = MovieRecordValidator.validateMovieRecord(movieRental);

        assertThat(result.valid()).isFalse();
        assertThat(result.errorMessage()).isEqualTo("Invalid movie ID: InvalidID");
    }

    @Test
    public void givenMovieRentalWithNegativeDaysRented_whenValidateMovieRecord_thenReturnsFailure() {
        MovieRental movieRental = new MovieRental("F001", -1);

        ValidationResult<MovieEnum> result = MovieRecordValidator.validateMovieRecord(movieRental);

        assertThat(result.valid()).isFalse();
        assertThat(result.errorMessage()).isEqualTo("Invalid rental record for F001, daysRented value outside of bounds: -1");
    }

    @Test
    public void givenMovieRentalWithDaysRentedExceedingLimit_whenValidateMovieRecord_thenReturnsFailure() {
        MovieRental movieRental = new MovieRental("F001", 36525); // > 100 years in days

        ValidationResult<MovieEnum> result = MovieRecordValidator.validateMovieRecord(movieRental);

        assertThat(result.valid()).isFalse();
        assertThat(result.errorMessage()).isEqualTo("Invalid rental record for F001, daysRented value outside of bounds: 36525");
    }

}
