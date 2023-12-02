package com.enquiry.domain.enquiry.repositories;

import com.enquiry.domain.common.IBaseRepository;
import com.enquiry.domain.enquiry.Enquiry;

import reactor.core.publisher.Flux;

/**
 * Commands and queries repository can be different but it all depends on which kind of 
 * CQRS implementation is there, in this project commands and queries requires same 
 * relational database , so using CQRS with single database.
 * @author prakumar113
 *
 */
public interface IEnquiryRepository extends IBaseRepository<Enquiry, String>{
	public Flux<Enquiry> findAllByApartmentID(String id);
}
