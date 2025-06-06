package com.etraveli.practice.repository;

import com.etraveli.practice.dto.Movie;
import com.etraveli.practice.dto.MovieCode;
import com.etraveli.practice.repository.impl.MockMovieRepository;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class MockMovieRepositoryTest {

    private final MockMovieRepository movieRepository;

    public MockMovieRepositoryTest() {
        this.movieRepository = new MockMovieRepository();
    }

    @Test
    public void givenValidMovieRecord_whenGetMovieById_thenReturnsMovie() {
        Movie result = movieRepository.getMovieById("F001");

        assertThat(result).isNotNull();
        assertThat(result.title()).isEqualTo("You've Got Mail");
        assertThat(result.code()).isEqualTo(MovieCode.REGULAR);
    }

    @Test
    public void givenMovieRecordWithInvalidId_whenGetMovieById_thenReturnsNull() {
        Movie result = movieRepository.getMovieById("Invalid");

        assertThat(result).isNull();
    }

}
