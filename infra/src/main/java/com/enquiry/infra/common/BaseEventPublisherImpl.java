package com.enquiry.infra.common;

import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate;
import org.springframework.stereotype.Service;

import com.enquiry.domain.common.DomainModel;
import com.enquiry.domain.common.IBaseRepository;
import com.enquiry.domain.common.IEventPublisher;
import com.google.gson.Gson;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Base Event Publisher implementation , this gets list of domain events for an domain object
 * and publish it to kafka topics with same name. Please create other implementation of @IEventPublisher
 * as per business requirement if this doesn't suits.
 * 
 * @author prakumar113
 *
 * @param <E>
 * @param <T>
 */
@Service
@AllArgsConstructor
@Slf4j
public abstract class BaseEventPublisherImpl<E extends DomainModel<?> , T> implements IEventPublisher<E , T>{
	private final IBaseRepository<E , T> repository;
	private final ReactiveKafkaProducerTemplate<String, String> reactiveKafkaProducerTemplate;

	private final Gson gson;

	@SuppressWarnings("unchecked")
	@Override
	public Mono<E> publish(E object) {
		return Flux.fromStream(object.getDomainEvents().stream())
			.flatMap(event -> reactiveKafkaProducerTemplate.send(event.name(), gson.toJson(object)))
			.then(Mono.just(object))
			.onErrorResume(throwable -> {
				log.error("Unable to create enquiry, error {}", throwable);
				repository.delete((T) object.getId()).subscribe();
				return Mono.just(object);
			});
	}
}
