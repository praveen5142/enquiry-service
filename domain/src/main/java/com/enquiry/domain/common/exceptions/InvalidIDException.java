package com.enquiry.domain.common.exceptions;

public class InvalidIDException extends DomainException{
	private static final long serialVersionUID = 8736305551128339739L;
	public InvalidIDException(String message) {
		super(message);
	}
}
