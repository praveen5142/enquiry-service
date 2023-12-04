package com.enquiry.domain.common;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IBaseRepository <E , T>{
	public Mono<E> save(E e);
	public Flux<E> findAll();
	public Mono<E> findById(T id);
	public Mono<Long> delete(T id);
	public Mono<E> update(E e);
	public Mono<Boolean> available(T id);
	public Mono<Void> createSchema();
	abstract Mono<E> populate(E e);
	abstract Flux<E> populateAll(Flux<E> e);
}
