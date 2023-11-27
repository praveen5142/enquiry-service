package com.enquiry.domain.common;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

public abstract class DomainModel<T extends Enum<T>> implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Getter @Setter protected String id;
	@Getter protected DomainEvents<T> domainEvents = new DomainEvents<T>();
	
	public void addDomainEvents(T event) {
		this.domainEvents.getEventNames().add(event);
	}
}
