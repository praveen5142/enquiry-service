package com.enquiry.domain.enquiry.services;

import com.enquiry.domain.enquiry.Enquiry;
import com.enquiry.domain.enquiry.entities.OwnerUpdate;

import reactor.core.publisher.Mono;

/**
 * Create separate commands and queries package in case of CQRS with separate database.
 * @author prakumar113
 */
public interface IEnquiryCommand {
	public Mono<Enquiry> createEnquiry(Enquiry enquiry);
	public Mono<Enquiry> updateEnquiry(Enquiry enquiry);
	public Mono<Enquiry> ownerUpdate(OwnerUpdate ownerUpdate);
}
