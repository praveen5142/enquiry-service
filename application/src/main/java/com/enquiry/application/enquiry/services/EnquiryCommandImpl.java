package com.enquiry.application.enquiry.services;

import org.springframework.stereotype.Service;

import com.enquiry.domain.common.IEventPublisher;
import com.enquiry.domain.enquiry.Enquiry;
import com.enquiry.domain.enquiry.EnquiryConstants.ENQUIRY_EVENTS;
import com.enquiry.domain.enquiry.entities.OwnerUpdate;
import com.enquiry.domain.enquiry.repositories.IEnquiryRepository;
import com.enquiry.domain.enquiry.services.IEnquiryCommand;

import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;

@AllArgsConstructor
@Service
public class EnquiryCommandImpl implements IEnquiryCommand {
	private final IEnquiryRepository repository;
	private final IEventPublisher<Enquiry> publisher;

	@Override
	public Mono<Enquiry> createEnquiry(Enquiry enquiry) {
		return this.repository.save(enquiry).doOnSuccess(publisher::publish);
	}
	
	@Override
	public Mono<Enquiry> updateEnquiry(Enquiry enquiry) {
		return this.repository.findById(enquiry.getId())
				.doOnSuccess(value -> value.updateEnquiry(value.getRequestedPrice().getAmount(), value.getRequestedPrice().getCurrency().name(),
						value.getTenentDescription(), value.getTotalPersons(), value.getRelationship()))
				.doOnSuccess(this.repository::save)
				.doOnSuccess(publisher::publish);
	}

	@Override
	public Mono<Enquiry> ownerUpdate(OwnerUpdate ownerUpdate) {
		return this.repository.addOwnerUpdate(ownerUpdate)
				.doOnSuccess(publisher::publish).map(enquiry-> {
					enquiry.addDomainEvents(ENQUIRY_EVENTS.valueOf(ownerUpdate.getStatus()));
					return enquiry;
				});
	}
}
