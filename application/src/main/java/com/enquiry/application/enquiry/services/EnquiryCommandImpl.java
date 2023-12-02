package com.enquiry.application.enquiry.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.enquiry.domain.common.IEventPublisher;
import com.enquiry.domain.common.exceptions.DomainException;
import com.enquiry.domain.enquiry.Enquiry;
import com.enquiry.domain.enquiry.entities.OwnerUpdate;
import com.enquiry.domain.enquiry.repositories.IEnquiryRepository;
import com.enquiry.domain.enquiry.repositories.IOwnerUpdateRepository;
import com.enquiry.domain.enquiry.services.IEnquiryCommand;

import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;

@AllArgsConstructor
@Service
public class EnquiryCommandImpl implements IEnquiryCommand {
	private final IEnquiryRepository enquiryRepository;
	private final IOwnerUpdateRepository ownerUpdateRepository;
	private final IEventPublisher<Enquiry, String> enquiryEventPublisher;
	private final IEventPublisher<OwnerUpdate, String> ownerUpdateEventPublisher;

	@Transactional
	@Override
	public Mono<Enquiry> createEnquiry(Enquiry enquiry) {
		return this.enquiryRepository.save(enquiry).doOnSuccess(enquiryEventPublisher::publish);
	}
	
	@Override
	public Mono<Enquiry> updateEnquiry(Enquiry enquiry) {
		return this.enquiryRepository.findById(enquiry.getId()).flatMap(dbEnquiry -> {
			try {
				dbEnquiry.updateEnquiry(enquiry.getRequestedPrice(), enquiry.getTenentDescription(),
						enquiry.getTotalPersons(), enquiry.getRelationship());
				return this.enquiryRepository.save(dbEnquiry).doOnSuccess(enquiryEventPublisher::publish);
			} catch (DomainException e) {
				return Mono.error(e);
			}
		});
	}

	@Override
	public Mono<Enquiry> ownerUpdate(OwnerUpdate ownerUpdate) {
		return this.ownerUpdateRepository.save(ownerUpdate).doOnSuccess(ownerUpdateEventPublisher::publish)
				.flatMap(dbUpdate->{
					return this.enquiryRepository.findById(dbUpdate.getEnquiryID());
				});
	}
}
