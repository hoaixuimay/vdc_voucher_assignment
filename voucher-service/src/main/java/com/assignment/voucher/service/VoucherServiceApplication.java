package com.assignment.voucher.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = {"com.assignment.voucher.service.repository"})
public class VoucherServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(VoucherServiceApplication.class, args);
	}

}
