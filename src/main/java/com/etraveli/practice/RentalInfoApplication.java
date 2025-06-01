package com.etraveli.practice;

import com.etraveli.practice.dto.Customer;
import com.etraveli.practice.service.RentalInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.function.Consumer;
import java.util.function.Supplier;

@SpringBootApplication(scanBasePackages = "com.etraveli.practice")
public class RentalInfoApplication {

    private final RentalInfoService rentalInfoService;
    private final Supplier<Customer> customerSupplier;
    private final Consumer<String> resultConsumer;

    @Autowired
    public RentalInfoApplication(RentalInfoService rentalInfoServiceImpl, Supplier<Customer> defaultCustomerSupplier, Consumer<String> stringResultConsumer) {
        this.rentalInfoService = rentalInfoServiceImpl;
        this.customerSupplier = defaultCustomerSupplier;
        this.resultConsumer = stringResultConsumer;
    }

    //P.S. Test task aside, there really shouldn't be any calls like this here as we'd want to have a REST endpoint handling calls to the service
    public static void main(String[] args) {
        RentalInfoApplication rentalService = SpringApplication.run(RentalInfoApplication.class, args).getBean(RentalInfoApplication.class);
        rentalService.processStatement();
    }

    private void processStatement() {
        Customer customer = customerSupplier.get();
        String result = rentalInfoService.generateStatementForCustomer(customer);
        resultConsumer.accept(result);
    }

}
