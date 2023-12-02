package com.enquiry.infra.common;

public class DatabaseException extends RuntimeException {
	private static final long serialVersionUID = -6348649801836137992L;

	public DatabaseException(String message) {
		super(message);
	}
}