package com.enquiry.infra.enquiry.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.enquiry.domain.enquiry.Enquiry;
import com.enquiry.domain.enquiry.entities.OwnerUpdate;
import com.enquiry.domain.enquiry.repositories.IEnquiryRepository;
import com.enquiry.infra.common.BaseRepositoryImpl;
import com.enquiry.infra.common.IDatabaseOperations;
import com.enquiry.infra.enquiry.mappers.EnquiryDatabaseOperations;
import com.enquiry.infra.enquiry.mappers.OwnerUpdateDatabaseOperations;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public class EnquiryRepository extends BaseRepositoryImpl<Enquiry , String> implements IEnquiryRepository{
	private final DatabaseClient databaseClient;
	private final IDatabaseOperations<Enquiry> enquiryOperations;
	private final IDatabaseOperations<OwnerUpdate> ownerUpdateOperations;
	
	public EnquiryRepository(@Autowired DatabaseClient databaseClient , @Autowired IDatabaseOperations<Enquiry> enquiryOperations,
			@Autowired IDatabaseOperations<OwnerUpdate> ownerUpdateOperations) {
		super(databaseClient, Enquiry.TABLE_NAME, EnquiryDatabaseOperations.SCHEMA , enquiryOperations);
		this.databaseClient = databaseClient;
		this.enquiryOperations = enquiryOperations;
		this.ownerUpdateOperations = ownerUpdateOperations;
	}
	
	
	@Override
	public Flux<Enquiry> findAllByApartmentID(String id) {
		return databaseClient.sql(EnquiryDatabaseOperations.FETCH_ALL_BY_APARTMENT)
				.bind("apartmentID", id)
				.map((row, metaData) -> enquiryOperations.map(row))
				.all()
				.map(enq -> {
					findOwnerUpdatesByEnquiryID(enq.getId()).doOnNext(enq::addOwnerUpdate);
					return enq;
				});
	}

	@Override
	public Mono<Enquiry> populate(Enquiry e) {
		return this.findOwnerUpdatesByEnquiryID(e.getId()).doOnNext(e::addOwnerUpdate).then(Mono.just(e));
	}

	@Override
	public Flux<Enquiry> populateAll(Flux<Enquiry> e) {
		return this.findAll().doOnNext(this::populate);
	}

	@Override
	public Flux<OwnerUpdate> findOwnerUpdatesByEnquiryID(String enquiryID) {
		return databaseClient.sql(OwnerUpdateDatabaseOperations.FIND_BY_ENQID)
				.bind("enquiryID", enquiryID)
				.map((row, metaData) -> ownerUpdateOperations.map(row))
				.all();
	}

	@Transactional
	@Override
	public Mono<OwnerUpdate> addOwnerUpdate(OwnerUpdate ownerUpdate) {
		return this.findById(ownerUpdate.getEnquiryID()).doOnSuccess(enq -> enq.addNewOwnerUpdate(ownerUpdate))
				.doOnSuccess(this::save)
				.doOnSuccess(this::saveOwnerUpdate)
				.then(Mono.just(ownerUpdate));
	}
	
	private final Mono<Enquiry> saveOwnerUpdate(Enquiry enquiry) {
		return Flux.fromStream(enquiry.getOwnerUpdates().stream())
				.doOnNext(ownerUpdate->
					this.databaseClient.sql(ownerUpdateOperations.createInsertQuery())
					.bindValues(ownerUpdateOperations.createBindings(enquiry.getOwnerUpdates().get(0)))
					.fetch().rowsUpdated()
					.switchIfEmpty(Mono.error(new RuntimeException("Unable to insert into "+OwnerUpdate.TABLE_NAME+", check logs")))
				).then(Mono.just(enquiry));
	}
}