package com.enquiry.infra.enquiry.handlers;

import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate;
import org.springframework.stereotype.Service;

import com.enquiry.domain.common.IEventPublisher;
import com.enquiry.domain.enquiry.Enquiry;
import com.enquiry.domain.enquiry.repositories.IEnquiryRepository;
import com.google.gson.Gson;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@AllArgsConstructor
@Slf4j
@Service
public class EnquriyEventPublisher implements IEventPublisher<Enquiry>{
	private final IEnquiryRepository repository;

	private final ReactiveKafkaProducerTemplate<String, String> reactiveKafkaProducerTemplate;

	private final Gson gson;

	@Override
	public Mono<Enquiry> publish(Enquiry object) {
		return Flux.fromStream(object.getDomainEvents().getEventNames().stream()).doOnNext(event -> {
			reactiveKafkaProducerTemplate.send(event.name(), gson.toJson(object))
			.doOnSuccess(senderResult -> log.info("sent {} offset : {}", object, senderResult.recordMetadata().offset()))
			.doOnError(throwable -> {
				repository.delete(object.getId()).subscribe();
				log.error("Unable to create enquiry, error {}", throwable);
			}).subscribe();
		}).then().thenReturn(object);
	}

}
