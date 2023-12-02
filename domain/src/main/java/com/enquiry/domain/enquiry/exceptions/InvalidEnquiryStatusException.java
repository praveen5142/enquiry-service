package com.enquiry.domain.enquiry.exceptions;

import com.enquiry.domain.common.exceptions.DomainException;

public class InvalidEnquiryStatusException extends DomainException {
	private static final long serialVersionUID = -6348649801836137992L;

	public InvalidEnquiryStatusException(String message) {
		super(message);
	}
}
