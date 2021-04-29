package com.openkm.aceptable;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jms.JmsAutoConfiguration;

@SpringBootApplication(exclude = JmsAutoConfiguration.class)
public class AceptableApplication {

	public static void main(String[] args) {
		SpringApplication.run(AceptableApplication.class, args);
	}

}
