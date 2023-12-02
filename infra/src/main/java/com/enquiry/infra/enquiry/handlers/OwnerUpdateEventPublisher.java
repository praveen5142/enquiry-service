package com.enquiry.infra.enquiry.handlers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate;
import org.springframework.stereotype.Service;

import com.enquiry.domain.enquiry.entities.OwnerUpdate;
import com.enquiry.infra.common.BaseEventPublisherImpl;
import com.enquiry.infra.enquiry.repositories.OwnerUpdateRepository;
import com.google.gson.Gson;

@Service("OwnerUpdateEventPublisher")
public class OwnerUpdateEventPublisher extends BaseEventPublisherImpl<OwnerUpdate , String>{

	public OwnerUpdateEventPublisher(@Autowired OwnerUpdateRepository repository,
			ReactiveKafkaProducerTemplate<String, String> reactiveKafkaProducerTemplate, Gson gson) {
		super(repository, reactiveKafkaProducerTemplate, gson);
	}
}
