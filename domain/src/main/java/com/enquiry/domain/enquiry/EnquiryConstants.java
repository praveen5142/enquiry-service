package com.enquiry.domain.enquiry;

public class EnquiryConstants {
	public static final String NOTIFICATION_STATUS = "NOTIFICATION_STATUS";
	public static final String TENANT_STATUS = "TENANT_STATUS";
	
	private EnquiryConstants() {}
	
	public static enum RELATIONSHIPS {
		BACHLEOUR , MARRIED , LIVIN ;
	}
	
	public enum ENQUIRY_EVENTS {
		NEW_ENQUIRY , ENQUIRY_UPDATED , OWNER_APPROVED , OWNER_REJECTED , ENQUIRY_UPDATE_REQUIRED;
	}
	
	public static enum CURRENCY{
		INR , USD , IDR , RUP;
	}
}