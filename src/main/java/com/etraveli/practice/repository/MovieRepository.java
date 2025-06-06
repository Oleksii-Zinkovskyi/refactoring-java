package com.etraveli.practice.repository;

import com.etraveli.practice.dto.Movie;

public interface MovieRepository {

    Movie getMovieById(String movieCode);

}
