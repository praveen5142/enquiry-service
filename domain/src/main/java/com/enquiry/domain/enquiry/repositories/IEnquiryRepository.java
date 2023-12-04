package com.enquiry.domain.enquiry.repositories;

import com.enquiry.domain.common.IBaseRepository;
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
public interface IEnquiryRepository extends IBaseRepository<Enquiry, String>{
	public Flux<Enquiry> findAllByApartmentID(String id);
	Flux<OwnerUpdate> findOwnerUpdatesByEnquiryID(String enquiryID);
	
	/**
	 * Did not provided IOwnerUpdateRepository , no need to provide interfaces for entities inside a 
	 * aggregate , it will not enforce transactional boundaries on the aggregate otherwise.
	 * @param enquiryID
	 * @return
	 */
	public Mono<OwnerUpdate> addOwnerUpdate(OwnerUpdate ownerUpdate);
}
