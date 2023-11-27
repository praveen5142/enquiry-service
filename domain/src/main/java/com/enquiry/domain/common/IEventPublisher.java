package com.enquiry.domain.common;

import reactor.core.publisher.Mono;

/**
 * Domain events should be created as ENUMS and this is to ensure 
 * only DomainModel classes are passed as those will have Domain Events.
 * @author prakumar113
 *
 * @param <T>
 */
public interface IEventPublisher<T extends DomainModel<?>> {
	public Mono<T> publish(T object);
}
