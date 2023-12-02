package com.enquiry.infra.enquiry.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;

import com.enquiry.domain.enquiry.entities.OwnerUpdate;
import com.enquiry.domain.enquiry.repositories.IOwnerUpdateRepository;
import com.enquiry.infra.common.BaseRepositoryImpl;
import com.enquiry.infra.common.IDatabaseOperations;
import com.enquiry.infra.enquiry.mappers.OwnerUpdateDatabaseOperations;

import reactor.core.publisher.Flux;

@Repository
public class OwnerUpdateRepository extends BaseRepositoryImpl<OwnerUpdate , String> implements IOwnerUpdateRepository{
	

	private final DatabaseClient databaseClient;
	private final IDatabaseOperations<OwnerUpdate> databaseOperations;
	
	public OwnerUpdateRepository(@Autowired DatabaseClient databaseClient, @Autowired IDatabaseOperations<OwnerUpdate> databaseOperations) {
		super(databaseClient, OwnerUpdate.TABLE_NAME , OwnerUpdateDatabaseOperations.SCHEMA , databaseOperations);
		this.databaseClient = databaseClient;
		this.databaseOperations = databaseOperations;
	}

	@Override
	public Flux<OwnerUpdate> findAllByEnquiryID(String enquiryID) {
		return databaseClient.sql(OwnerUpdateDatabaseOperations.FIND_BY_ENQID)
				.bind("enquiryID", enquiryID)
				.map((row, metaData) -> databaseOperations.map(row))
				.all();
	}
}