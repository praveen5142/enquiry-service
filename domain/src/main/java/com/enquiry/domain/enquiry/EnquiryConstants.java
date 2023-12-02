package com.enquiry.domain.enquiry;

import com.enquiry.domain.enquiry.exceptions.InvalidEnquiryStatusException;

public class EnquiryConstants {
	public static final String NOTIFICATION_STATUS = "NOTIFICATION_STATUS";
	public static final String TENANT_STATUS = "TENANT_STATUS";
	
	private EnquiryConstants() {}
	
	public enum RELATIONSHIPS {
		BACHLEOUR , MARRIED , LIVIN ;
		
		public static void validate(String value) throws InvalidEnquiryStatusException{
			try {
				valueOf(value);
			}catch (IllegalArgumentException e) {
				throw new InvalidEnquiryStatusException(e.getMessage());
			}
		}
	}
	
	public enum ENQUIRY_EVENTS {
		NEW_ENQUIRY , ENQUIRY_UPDATED;
		
		public static void validate(String value) throws InvalidEnquiryStatusException{
			try {
				valueOf(value);
			}catch (IllegalArgumentException e) {
				throw new InvalidEnquiryStatusException(e.getMessage());
			}
		}
	}
	
	public enum OWNER_UPDATE_EVENTS {
		 OWNER_APPROVED , OWNER_REJECTED , ENQUIRY_UPDATE_REQUIRED;
		
		public static void validate(String value) throws InvalidEnquiryStatusException{
			try {
				valueOf(value);
			}catch (IllegalArgumentException e) {
				throw new InvalidEnquiryStatusException(e.getMessage());
			}
		}
	}
	
	public enum CURRENCY{
		INR , USD , IDR , RUP;
		
		public static void validate(String value) throws InvalidEnquiryStatusException{
			try {
				valueOf(value);
			}catch (IllegalArgumentException e) {
				throw new InvalidEnquiryStatusException(e.getMessage());
			}
		}
	}
}