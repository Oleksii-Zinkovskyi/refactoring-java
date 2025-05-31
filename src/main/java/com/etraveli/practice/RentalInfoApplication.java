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

  private final RentalInfoService rentalInfoService;

  @Autowired
  public RentalInfoApplication(RentalInfoService rentalInfoServiceImpl) {
    this.rentalInfoService = rentalInfoServiceImpl;
  }

  //TODO: Switch to some sort of generic consumer/supplier combo that would read this input from default properties?
  //There really shouldn't be a call to test method in the main method, but I suppose it simulates user input in a way. Ideally we'd want to have a REST endpoint calling this
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
    return rentalInfoService.generateStatementForCustomer(customer);
  }

}
