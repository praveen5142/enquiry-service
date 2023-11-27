package com.enquiry.contracts.common;

import java.io.Serializable;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SuccessResponse implements Serializable{
	private static final long serialVersionUID = -6033161713569360291L;
	
	private LocalDate timestamp;
}
