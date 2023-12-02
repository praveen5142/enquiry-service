package com.enquiry.infra.common.configurations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;

import com.enquiry.infra.enquiry.repositories.EnquiryRepository;
import com.enquiry.infra.enquiry.repositories.OwnerUpdateRepository;

import lombok.extern.slf4j.Slf4j;


@EnableR2dbcRepositories
@Configuration
@Slf4j
public class DatabaseConfigurer implements CommandLineRunner{

	@Autowired
	EnquiryRepository repository;
	
	@Autowired
	OwnerUpdateRepository ownerUpdateRepository;
	
	
	@Override
	public void run(String... args) throws Exception {
		log.info("Starting database schema preparation!");
		ownerUpdateRepository.createSchema()
			.subscribe();
		log.info("Database configured!");
	}
}
