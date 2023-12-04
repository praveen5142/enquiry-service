package com.enquiry.infra.enquiry.handlers;

import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate;
import org.springframework.stereotype.Service;

import com.enquiry.domain.common.IBaseRepository;
import com.enquiry.domain.common.IEventPublisher;
import com.enquiry.domain.enquiry.Enquiry;
import com.google.gson.Gson;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service("EnquiryEventPublisher")
@Slf4j
@AllArgsConstructor
public class EnquiryEventPublisher implements IEventPublisher<Enquiry>{

	private final IBaseRepository<Enquiry , String> repository;
	private final ReactiveKafkaProducerTemplate<String, String> reactiveKafkaProducerTemplate;

	private final Gson gson;

	@Override
	public Mono<Enquiry> publish(Enquiry object) {
		return Flux.fromStream(object.getDomainEvents().stream())
			.flatMap(event -> reactiveKafkaProducerTemplate.send(event.name(), gson.toJson(object)))
			.then(Mono.just(object))
			.onErrorResume(throwable -> {
				log.error("Unable to create enquiry, error {}", throwable);
				repository.delete(object.getId()).subscribe();
				return Mono.just(object);
			});
	}
}
