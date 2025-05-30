package com.etraveli.practice.service;

import com.etraveli.practice.dto.Customer;
import com.etraveli.practice.dto.MovieEnum;
import com.etraveli.practice.dto.MovieRental;
import org.springframework.stereotype.Service;

@Service
public class RentalInfoService {

  public String statement(Customer customer) {
    MovieEnum movie;
    double totalAmount = 0;
    int frequentEnterPoints = 0;
    String result = "Rental Record for " + customer.name() + "\n";
    for (MovieRental r : customer.rentals()) {
      try {
        movie = MovieEnum.valueOf(r.movieId());
      } catch (IllegalArgumentException e) {
        //There could be sophisticated logic for handling this wherein we set an error flag but proceed with the rest of the rentals
        throw new IllegalArgumentException("Invalid movie ID: " + r.movieId());
      }
      double thisAmount = 0;

      // determine amount for each movie
      if (movie.getCode().equals("regular")) {
        thisAmount = 2;
        if (r.days() > 2) {
          thisAmount = ((r.days() - 2) * 1.5) + thisAmount;
        }
      }
      if (movie.getCode().equals("new")) {
        thisAmount = r.days() * 3;
      }
      if (movie.getCode().equals("childrens")) {
        thisAmount = 1.5;
        if (r.days() > 3) {
          thisAmount = ((r.days() - 3) * 1.5) + thisAmount;
        }
      }

      //add frequent bonus points
      frequentEnterPoints++;
      // add bonus for a two day new release rental
      if (movie.getCode() == "new" && r.days() > 2) frequentEnterPoints++;

      //print figures for this rental
      result += "\t" + movie.getTitle() + "\t" + thisAmount + "\n";
      totalAmount = totalAmount + thisAmount;
    }
    // add footer lines
    result += "Amount owed is " + totalAmount + "\n";
    result += "You earned " + frequentEnterPoints + " frequent points\n";

    return result;
  }

}
