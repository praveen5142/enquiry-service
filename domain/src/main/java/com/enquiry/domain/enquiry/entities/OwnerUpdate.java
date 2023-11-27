package com.enquiry.domain.enquiry.entities;

import java.io.Serializable;
import java.util.UUID;

import com.enquiry.domain.enquiry.EnquiryConstants.ENQUIRY_EVENTS;
import com.enquiry.domain.enquiry.valueobjects.Price;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Synchronized;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class OwnerUpdate implements Serializable{
	private OwnerUpdate() {}
	private static final long serialVersionUID = -1279927392220521573L;
	
	@EqualsAndHashCode.Include
	private String id;
	private String enquiryId;
	private Price negotiatedPrice;
	private String status;
	private String remarks;
	
	
	public final boolean ownerAccepted() {
		return ENQUIRY_EVENTS.OWNER_APPROVED.equals(ENQUIRY_EVENTS.valueOf(this.status));
	}
	
	@Synchronized
	public static final OwnerUpdate create(String enquiryId , Price negotiatedPrice , String status , 
			String remarks) {
		OwnerUpdate ownerUpdate = new OwnerUpdate();
		ownerUpdate.setId(UUID.randomUUID().toString());
		ownerUpdate.setEnquiryId(enquiryId);
		ownerUpdate.setNegotiatedPrice(negotiatedPrice);
		ownerUpdate.setStatus(status);
		ownerUpdate.setRemarks(remarks);
		return ownerUpdate;
	}
	
	public static final OwnerUpdate getEmptyInstance() { return new OwnerUpdate(); }
}