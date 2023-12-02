package com.enquiry.domain.enquiry.entities;

import com.enquiry.domain.common.DomainModel;
import com.enquiry.domain.common.exceptions.DomainException;
import com.enquiry.domain.enquiry.EnquiryConstants.OWNER_UPDATE_EVENTS;
import com.enquiry.domain.enquiry.exceptions.MissingEnquiryIDException;
import com.enquiry.domain.enquiry.valueobjects.Price;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@EqualsAndHashCode(callSuper = true , onlyExplicitlyIncluded = true)
public class OwnerUpdate extends DomainModel<OWNER_UPDATE_EVENTS>{
	private static final long serialVersionUID = -1279927392220521573L;
	public static final String TABLE_NAME = "owner_updates";
	@Getter private final String enquiryID;
	@Getter private final Price negotiatedPrice;
	@Getter private final String status;
	@Getter private final String remarks;
	
	// new object
	public OwnerUpdate(String enquiryID, Price negotiatedPrice, String status, String remarks) throws DomainException{
		super(null);
		this.enquiryID = enquiryID;
		this.negotiatedPrice = negotiatedPrice;
		this.status = status;
		this.remarks = remarks;
		addDomainEvent(OWNER_UPDATE_EVENTS.valueOf(status));
		validate();
	}
	
	public OwnerUpdate(String id, String enquiryID, Price negotiatedPrice, String status, String remarks) throws DomainException{
		super(id);
		this.enquiryID = enquiryID;
		this.negotiatedPrice = negotiatedPrice;
		this.status = status;
		this.remarks = remarks;
		validate();
	}
	
	public final boolean ownerAccepted() {
		return OWNER_UPDATE_EVENTS.OWNER_APPROVED.equals(OWNER_UPDATE_EVENTS.valueOf(this.status));
	}
	
	@Override
	protected void validate() throws DomainException {
		super.validateId(this.id);
		super.validateId(this.enquiryID);
		
		if(this.enquiryID == null ) {
			throw new MissingEnquiryIDException("Enquiry ID cannot be null, provided value: "+ id);
		}
		
		OWNER_UPDATE_EVENTS.validate(this.status);
	}
}