package com.etraveli.practice;

import com.etraveli.practice.consumer.GenericConsumer;
import com.etraveli.practice.dto.Customer;
import com.etraveli.practice.service.RentalInfoService;
import com.etraveli.practice.supplier.GenericSupplier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.etraveli.practice")
public class RentalInfoApplication {

  private final RentalInfoService rentalInfoService;
  private final GenericSupplier<Customer> customerSupplier;
  private final GenericConsumer<String> resultConsumer;

  @Autowired
  public RentalInfoApplication(RentalInfoService rentalInfoServiceImpl, GenericSupplier<Customer> defaultCustomerSupplier, GenericConsumer<String> stringResultConsumer) {
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
