package com.enquiry.domain.common;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import com.enquiry.domain.common.exceptions.DomainException;
import com.enquiry.domain.common.exceptions.InvalidIDException;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Synchronized;

/**
 * We have used UUID as IDs but if different aggregates requires different types of 
 * Primary IDS , create ValueObjects for each ID with its name. Like:
 * class OwnerUpdateID , class enquiryID and map it to ORM. this way if we need to change or 
 * support different kinds of IDs in our system its possible and mostly what professionals do.
 * 
 * DomainModels entities and aggregates are considerd same when id's are same not the whole object properties.
 * Thats why @EqualsAndHashCode.Include is placed over ID.
 * 
 * @author prakumar113
 *
 * @param <T>
 */
public abstract class DomainModel<T extends Enum<T>> implements Serializable{
	private static final long serialVersionUID = 1L;
	@EqualsAndHashCode.Include
	@Getter protected final String id;
	@Getter private Set<T> domainEvents = new HashSet<>();
	@Getter private final String tableName;
	
	protected abstract void validate() throws DomainException;
	
	protected DomainModel(final String id , final String tableName) {
		this.id = id == null ? this.createUUID() : id;
		this.tableName = tableName;
	}
	
	public void addDomainEvent(T event) {
		this.domainEvents.add(event);
	}
	
	public void removeDomainEvent(T event) {
		this.domainEvents.remove(event);
	}
	
	protected void validateId(String id) throws InvalidIDException{
		try {
			UUID.fromString(id);
		}catch (IllegalArgumentException e) {
			throw new InvalidIDException("Provided Primary Key ID is invalid UUID. Value :" + id);
		}
	}
	
	@Synchronized
	private final String createUUID() {
		return UUID.randomUUID().toString();
	}
}
