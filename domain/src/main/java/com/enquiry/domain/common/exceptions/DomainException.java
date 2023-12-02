package com.enquiry.domain.common.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

/** Domain exception class to provide flexiblity for devs to handle one or all 
 * domain exceptions at once ....instead of handling each domain exceptions.
 * @author prakumar113
 */

@AllArgsConstructor
public abstract class DomainException extends Exception{
	private static final long serialVersionUID = -6532433016207659554L;
	
	@Getter
	private final String message;
}
