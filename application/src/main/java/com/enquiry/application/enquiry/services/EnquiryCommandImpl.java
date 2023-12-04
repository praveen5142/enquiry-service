package com.enquiry.application.enquiry.services;

import org.springframework.stereotype.Service;

import com.enquiry.domain.common.IEventPublisher;
import com.enquiry.domain.common.exceptions.DomainException;
import com.enquiry.domain.enquiry.Enquiry;
import com.enquiry.domain.enquiry.entities.OwnerUpdate;
import com.enquiry.domain.enquiry.repositories.IEnquiryRepository;
import com.enquiry.domain.enquiry.services.IEnquiryCommand;

import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;

/**
 * No need to add @Transactional here as aggregates have consistence/transactional boundaries. That is aggregate object is 
 * updated as a whole. Its repository responsiblity to make it @Transactional or handle it.
 * 
 * @author prakumar113
 */

@AllArgsConstructor
@Service
public class EnquiryCommandImpl implements IEnquiryCommand {
	private final IEnquiryRepository repository;
	private final IEventPublisher<Enquiry> enquiryEventPublisher;
	private final IEventPublisher<OwnerUpdate> ownerUpdateEventPublisher;

	@Override
	public Mono<Enquiry> createEnquiry(Enquiry enquiry) {
		return this.repository.save(enquiry).doOnSuccess(enquiryEventPublisher::publish);
	}
	
	@Override
	public Mono<Enquiry> updateEnquiry(Enquiry enquiry) {
		return this.repository.findById(enquiry.getId()).flatMap(dbEnquiry -> {
			try {
				dbEnquiry.updateEnquiry(enquiry.getRequestedPrice(), enquiry.getTenentDescription(),
						enquiry.getTotalPersons(), enquiry.getRelationship());
				return this.repository.save(dbEnquiry).doOnSuccess(enquiryEventPublisher::publish);
			} catch (DomainException e) {
				return Mono.error(e);
			}
		});
	}

	@Override
	public Mono<Enquiry> ownerUpdate(OwnerUpdate ownerUpdate) {
		return this.repository.addOwnerUpdate(ownerUpdate).doOnSuccess(ownerUpdateEventPublisher::publish)
				.flatMap(dbUpdate -> repository.findById(dbUpdate.getEnquiryID()));
	}
}
