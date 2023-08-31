package ru.lowgraysky.web3;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class Web3Application {

  public static void main(String[] args) {
    SpringApplication.run(Web3Application.class, args);
  }

}
