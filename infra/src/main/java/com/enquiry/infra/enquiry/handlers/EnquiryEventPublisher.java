package com.enquiry.infra.enquiry.handlers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate;
import org.springframework.stereotype.Service;

import com.enquiry.domain.enquiry.Enquiry;
import com.enquiry.infra.common.BaseEventPublisherImpl;
import com.enquiry.infra.enquiry.repositories.EnquiryRepository;
import com.google.gson.Gson;

@Service("EnquiryEventPublisher")
public class EnquiryEventPublisher extends BaseEventPublisherImpl<Enquiry, String>{

	public EnquiryEventPublisher(@Autowired EnquiryRepository repository,
			ReactiveKafkaProducerTemplate<String, String> reactiveKafkaProducerTemplate, Gson gson) {
		super(repository, reactiveKafkaProducerTemplate, gson);
	}
}
