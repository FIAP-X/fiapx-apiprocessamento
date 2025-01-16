package com.fiapx.apiprocessamento;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ApiprocessamentoApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiprocessamentoApplication.class, args);
	}

}