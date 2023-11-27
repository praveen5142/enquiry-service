package com.enquiry.contracts.enquiry;

import java.io.Serializable;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
public class EnquiryUpdateRequest implements Serializable{
	private static final long serialVersionUID = 3641047270390795571L;
	
	private String id;
	private long requestedPrice;
	private String currency;
	private String tenentDescription;
	private int totalPersons;
	private String relationship;
}