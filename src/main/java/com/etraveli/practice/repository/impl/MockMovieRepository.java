package com.etraveli.practice.repository.impl;

import com.etraveli.practice.dto.Movie;
import com.etraveli.practice.dto.MovieCode;
import com.etraveli.practice.repository.MovieRepository;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
public class MockMovieRepository implements MovieRepository {

    private final HashMap<String, Movie> movies = new HashMap<>();

    public MockMovieRepository() {
        movies.put("F001", new Movie("You've Got Mail", MovieCode.REGULAR));
        movies.put("F002", new Movie("Matrix", MovieCode.REGULAR));
        movies.put("F003", new Movie("Cars", MovieCode.CHILDREN));
        movies.put("F004", new Movie("Fast & Furious X", MovieCode.NEW));
    }

    public Movie getMovieById(String movieId) {
        return movies.get(movieId);
    }

}
