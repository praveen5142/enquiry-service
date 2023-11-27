package com.enquiry.domain.common;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import lombok.Data;

@Data
public class DomainEvents<T extends Enum<T>> implements Serializable{
	private static final long serialVersionUID = 4187747062044253360L;
	private Set<T> eventNames = new HashSet<>();
}
