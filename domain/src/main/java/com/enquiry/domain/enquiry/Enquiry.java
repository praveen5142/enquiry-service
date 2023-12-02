package com.enquiry.domain.enquiry;

import java.util.ArrayList;
import java.util.List;

import com.enquiry.domain.common.DomainModel;
import com.enquiry.domain.common.exceptions.DomainException;
import com.enquiry.domain.enquiry.EnquiryConstants.CURRENCY;
import com.enquiry.domain.enquiry.EnquiryConstants.ENQUIRY_EVENTS;
import com.enquiry.domain.enquiry.EnquiryConstants.RELATIONSHIPS;
import com.enquiry.domain.enquiry.entities.OwnerUpdate;
import com.enquiry.domain.enquiry.exceptions.InvalidRelationshipException;
import com.enquiry.domain.enquiry.exceptions.MissingApartmentIDException;
import com.enquiry.domain.enquiry.valueobjects.Price;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * 
 * To support internarate exception for each 
 * exception. 
 * Reasons to ask for all required parameters instead of builder or an object
 * 1) Ensure aggregate creation logic is not violated in any application.
 * 2) We need to create as small aggregates as possible as we cannot have much larger 
 * 	  transactional boundaries , it lead to race conditions. soo this method create object 
 *    can have more arguments as it will not grow too much.
 * 3) Cannot use contracts as parameters as it will tightly couple the modules and other teams should 
 *    not have access to business logic implemented.
 * @param createEnquiryRequest
 * @return
 * @throws InvalidRelationshipException 
 */

@EqualsAndHashCode(callSuper = true , onlyExplicitlyIncluded = true)
public class Enquiry extends DomainModel<ENQUIRY_EVENTS>{
	private static final long serialVersionUID = -7684457618631108033L;
	public static final String TABLE_NAME = "enquiries";
	@Getter private String userID;
	
	/**
	 * Using OwnerUpdate instead of OwnerUpdateID as OwnerUpdate is only used
	 * in this Aggregate and race conditions should not occur. Only aggregateRoots will be 
	 * reffered using IDs , Entities should not exist in other aggregates.
	 */
	@Getter @Setter private List<OwnerUpdate> ownerUpdates = new ArrayList<>();
	@Getter private String apartmentID;
	@Getter private Price requestedPrice;
	@Getter private String tenentDescription;
	@Getter private long totalPersons;
	@Getter private String relationship;
	
	// new object
	public Enquiry(String userID, List<OwnerUpdate> ownerUpdates, String apartmentID, Price requestedPrice,
			String tenentDescription, long totalPersons, String relationship) throws DomainException{
		super(null , TABLE_NAME);
		this.userID = userID;
		if(ownerUpdates != null) this.ownerUpdates = ownerUpdates;
		this.apartmentID = apartmentID;
		this.requestedPrice = requestedPrice;
		this.tenentDescription = tenentDescription;
		this.totalPersons = totalPersons;
		this.relationship = relationship;
		
		/* add Domain events for newly created enquiry to publish info about this aggregate */
		addDomainEvent(ENQUIRY_EVENTS.NEW_ENQUIRY);
		
		validate();
	}
	
	public Enquiry(String id, String userID, List<OwnerUpdate> ownerUpdates, String apartmentID, Price requestedPrice,
			String tenentDescription, long totalPersons, String relationship) throws DomainException{
		super(id , TABLE_NAME);
		this.userID = userID;
		if(ownerUpdates != null) this.ownerUpdates = ownerUpdates;
		this.apartmentID = apartmentID;
		this.requestedPrice = requestedPrice;
		this.tenentDescription = tenentDescription;
		this.totalPersons = totalPersons;
		this.relationship = relationship;
		
		validate();
	}
	
	/** add business logic like this to make rich models instead of enemic ones **/
	public final boolean updateCompletedStatus() {
		return !ownerUpdates.isEmpty() && ownerUpdates.get(ownerUpdates.size() - 1).ownerAccepted();
	}

	public static final OwnerUpdate createOwnerUpdate(String enquiryID , Price requestedPrice, String status , String remarks) throws DomainException {
		return new OwnerUpdate(enquiryID, requestedPrice, status, remarks);
	}
	
	public void addOwnerUpdate(OwnerUpdate ownerUpdate) {
		this.ownerUpdates.add(ownerUpdate);
	}
	
	public void updateEnquiry(Price requestedPrice , final String tenentDescription, final long totalPersons, final String relationship) throws DomainException {
		CURRENCY requestedCurrency = requestedPrice.getCurrency();
		if(!requestedCurrency.equals(this.requestedPrice.getCurrency())) {
			 this.requestedPrice.convertCurrency(this.requestedPrice.getCurrency(), requestedCurrency);
		}else {
			this.requestedPrice.setAmount(requestedPrice.getAmount());
		}
		
		this.tenentDescription = tenentDescription;
		this.totalPersons = totalPersons;
		this.relationship = relationship;
		addDomainEvent(ENQUIRY_EVENTS.ENQUIRY_UPDATED);
		
		validate();
	}

	@Override
	protected void validate() throws DomainException {
		super.validateId(this.id);
		super.validateId(this.apartmentID);
		super.validateId(this.userID);
		RELATIONSHIPS.validate(this.relationship);	
		
		if(this.totalPersons > 1 && !EnquiryConstants.RELATIONSHIPS.BACHLEOUR.name().equals(this.relationship)) {
			throw new InvalidRelationshipException("Relationship cannot be other than bachleour if total persons are 1");
		}
		if(this.apartmentID == null) {
			throw new MissingApartmentIDException("Missing apartment ID, provided value : " + this.apartmentID);
		}
	}
}
