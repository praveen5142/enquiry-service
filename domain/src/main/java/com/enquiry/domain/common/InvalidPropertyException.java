package com.enquiry.domain.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class InvalidPropertyException extends RuntimeException{
	private static final long serialVersionUID = 8736305551128339739L;
	
	@Getter
	private final String message;
}
