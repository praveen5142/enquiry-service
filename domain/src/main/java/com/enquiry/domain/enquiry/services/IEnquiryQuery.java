package com.enquiry.domain.enquiry.services;

import com.enquiry.domain.enquiry.Enquiry;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IEnquiryQuery {
	public Flux<Enquiry> getAll();
	public Mono<Enquiry> getEnquiry(String id);
	public Flux<Enquiry> getAllByAparmentId(String apartmentId);
}
