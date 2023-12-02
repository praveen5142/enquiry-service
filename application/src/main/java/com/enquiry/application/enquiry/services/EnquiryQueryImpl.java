package com.enquiry.application.enquiry.services;

import org.springframework.stereotype.Service;

import com.enquiry.domain.enquiry.Enquiry;
import com.enquiry.domain.enquiry.repositories.IEnquiryRepository;
import com.enquiry.domain.enquiry.repositories.IOwnerUpdateRepository;
import com.enquiry.domain.enquiry.services.IEnquiryQuery;

import lombok.AllArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@AllArgsConstructor
@Service
public class EnquiryQueryImpl implements IEnquiryQuery{
	private final IEnquiryRepository repository;
	private final IOwnerUpdateRepository ownerUpdateRepository;
	
	@Override
	public Flux<Enquiry> getAll() {
		return repository.findAll().doOnNext(enq -> {
			ownerUpdateRepository.findAllByEnquiryID(enq.getId()).log("Executed getAll").doOnNext(enq::addOwnerUpdate);
		});
	}

	@Override
	public Mono<Enquiry> getEnquiry(String id) {
		return repository.findById(id).doOnNext(enq -> { 
			ownerUpdateRepository.findAllByEnquiryID(enq.getId()).log("Executed getAll").doOnNext(enq::addOwnerUpdate);
		});
	}

	@Override
	public Flux<Enquiry> getAllByAparmentId(String apartmentId) {
		return repository.findAllByApartmentID(apartmentId).doOnNext(enq -> {
			ownerUpdateRepository.findAllByEnquiryID(enq.getId()).log("Executed getAll").doOnNext(enq::addOwnerUpdate);
		});
	}
}
