package com.enquiry.contracts.enquiry;

import java.io.Serializable;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class CreateEnquiryRequest implements Serializable{
	private static final long serialVersionUID = 3641047270390795571L;
	
	@EqualsAndHashCode.Include
	private String userID;
	@EqualsAndHashCode.Include
	private String apartmentID;
	@EqualsAndHashCode.Include
	private long requestedPrice;
	private String currency;
	
	private String tenentDescription;
	private int totalPersons;
	private String relationship;
}
