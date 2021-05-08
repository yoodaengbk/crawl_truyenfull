package com.demo.thrift;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ThriftApplication {

    public static void main(String[] args) {
        try {
            SpringApplication.run(ThriftApplication.class, args);
        } catch (Exception e) {
            throw e;
        }

    }
}
