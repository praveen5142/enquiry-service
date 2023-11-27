package com.enquiry.infra.common.configurations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;

import com.enquiry.domain.enquiry.repositories.IEnquiryRepository;

import lombok.extern.slf4j.Slf4j;


@EnableR2dbcRepositories
@Configuration
@Slf4j
public class DatabaseConfigurer implements CommandLineRunner{

	@Autowired
	IEnquiryRepository repository;
	
	@Override
	public void run(String... args) throws Exception {
		log.info("Starting database schema preparation!");
		repository.createSchema().subscribe();
		log.info("Database configured!");
	}

}
