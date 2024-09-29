package com.coinchange;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
public class CoinChangeApplication {

	public static void main(String[] args) {
		SpringApplication.run(CoinChangeApplication.class, args);
	}

}
