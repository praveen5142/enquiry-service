package com.enquiry.application.enquiry.services;

import org.springframework.stereotype.Service;

import com.enquiry.domain.enquiry.Enquiry;
import com.enquiry.domain.enquiry.repositories.IEnquiryRepository;
import com.enquiry.domain.enquiry.services.IEnquiryQuery;

import lombok.AllArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@AllArgsConstructor
@Service
public class EnquiryQueryImpl implements IEnquiryQuery{
	private final IEnquiryRepository repository;
	
	@Override
	public Flux<Enquiry> getAll() {
		return repository.findAll();
	}

	@Override
	public Mono<Enquiry> getEnquiry(String id) {
		return repository.findById(id);
	}

	@Override
	public Flux<Enquiry> getAllByAparmentId(String apartmentId) {
		return repository.findAllByApartmentID(apartmentId);
	}
}
