package com.etraveli.practice.consumer;

import org.springframework.stereotype.Component;

import java.util.function.Consumer;

@Component
public class StringResultConsumer implements Consumer<String> {

    @Override
    public void accept(String result) {
        String expected = "Rental Record for C. U. Stomer\n\tYou've Got Mail\t3.5\n\tMatrix\t2.0\nAmount owed is 5.5\nYou earned 2 frequent points\n";
        if (!result.equals(expected)) {
            throw new AssertionError("Expected: " + System.lineSeparator() + expected + System.lineSeparator() + "Got: " + System.lineSeparator() + result);
        }
        System.out.println("Success");
    }

}
