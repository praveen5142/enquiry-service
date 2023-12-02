package com.enquiry.infra.enquiry.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;

import com.enquiry.domain.enquiry.Enquiry;
import com.enquiry.domain.enquiry.repositories.IEnquiryRepository;
import com.enquiry.infra.common.BaseRepositoryImpl;
import com.enquiry.infra.common.IDatabaseOperations;
import com.enquiry.infra.enquiry.mappers.EnquiryDatabaseOperations;

import reactor.core.publisher.Flux;

@Repository
public class EnquiryRepository extends BaseRepositoryImpl<Enquiry , String> implements IEnquiryRepository{
	
	private final DatabaseClient databaseClient;
	private final IDatabaseOperations<Enquiry> databaseOperations;
	
	public EnquiryRepository(@Autowired DatabaseClient databaseClient , @Autowired IDatabaseOperations<Enquiry> databaseOperations) {
		super(databaseClient, Enquiry.TABLE_NAME, EnquiryDatabaseOperations.SCHEMA , databaseOperations);
		this.databaseClient = databaseClient;
		this.databaseOperations = databaseOperations;
	}
	
	
	@Override
	public Flux<Enquiry> findAllByApartmentID(String id) {
		return databaseClient.sql(EnquiryDatabaseOperations.FETCH_ALL_BY_APARTMENT)
				.bind("apartmentID", id)
				.map((row, metaData) -> databaseOperations.map(row))
				.all();
	}
}