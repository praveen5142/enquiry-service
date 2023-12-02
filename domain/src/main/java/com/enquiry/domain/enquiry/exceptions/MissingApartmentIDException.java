package com.enquiry.domain.enquiry.exceptions;

import com.enquiry.domain.common.exceptions.DomainException;

public class MissingApartmentIDException extends DomainException{
	private static final long serialVersionUID = -4560079014799060219L;

	public MissingApartmentIDException(String message) {
		super(message);
	}
}
