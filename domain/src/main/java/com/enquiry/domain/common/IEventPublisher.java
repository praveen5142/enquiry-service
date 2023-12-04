package com.enquiry.domain.common;

import reactor.core.publisher.Mono;

/**
 * Domain events should be created as ENUMS and this is to ensure 
 * only DomainModel classes are passed as those will have Domain Events.
 * 
 * Also create separate implementation for each domain object instead of writing generic
 * one as business logic maybe different for each Event Publisher.
 * @author prakumar113
 *
 * @param <T>
 */
public interface IEventPublisher<E extends DomainModel<?>> {
	public Mono<E> publish(E e);
}
