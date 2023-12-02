package com.enquiry.domain.enquiry.exceptions;

import com.enquiry.domain.common.exceptions.DomainException;

public class InvalidRelationshipException extends DomainException{
	private static final long serialVersionUID = -6348649801836137992L;
	public InvalidRelationshipException(String message) {super(message);}
}
