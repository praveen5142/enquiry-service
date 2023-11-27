package com.enquiry.domain.enquiry.repositories;

import com.enquiry.domain.enquiry.Enquiry;
import com.enquiry.domain.enquiry.entities.OwnerUpdate;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Commands and queries repository can be different but it all depends on which kind of 
 * CQRS implementation is there, in this project commands and queries requires same 
 * relational database , so using CQRS with single database.
 * @author prakumar113
 *
 */
public interface IEnquiryRepository {
	public Mono<Enquiry> save(Enquiry enquiry);
	public Mono<Enquiry> update(Enquiry enquiry);
	public Flux<Enquiry> findAll();
	public Flux<Enquiry> findAllByApartmentId(String id);
	public Mono<Enquiry> findById(String id);
	public Mono<Long> delete(String id);
	public Mono<Void> createSchema();
	
	/* Owner update do not get another repository as its part of enquiry aggregate and have same transactional boundaries */
	public Mono<Enquiry> addOwnerUpdate(OwnerUpdate ownerUpdate);
}
