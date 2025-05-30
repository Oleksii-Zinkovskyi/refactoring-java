package com.etraveli.practice;

import com.etraveli.practice.dto.Customer;
import com.etraveli.practice.dto.MovieRental;
import com.etraveli.practice.service.RentalInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Arrays;

@SpringBootApplication(scanBasePackages = "com.etraveli.practice")
public class RentalInfoApplication {

  @Autowired
  private RentalInfoService rentalInfoService;

  public static void main(String[] args) {
    RentalInfoApplication rentalService = SpringApplication.run(RentalInfoApplication.class, args).getBean(RentalInfoApplication.class);

    String expected = "Rental Record for C. U. Stomer\n\tYou've Got Mail\t3.5\n\tMatrix\t2.0\nAmount owed is 5.5\nYou earned 2 frequent points\n";

    String result = rentalService.generateStatement(new Customer("C. U. Stomer", Arrays.asList(new MovieRental("F001", 3), new MovieRental("F002", 1))));

    if (!result.equals(expected)) {
      throw new AssertionError("Expected: " + System.lineSeparator() + String.format(expected) + System.lineSeparator() + System.lineSeparator() + "Got: " + System.lineSeparator() + result);
    }

    System.out.println("Success");
  }

  private String generateStatement(Customer customer) {
    return rentalInfoService.statement(customer);
  }

}
