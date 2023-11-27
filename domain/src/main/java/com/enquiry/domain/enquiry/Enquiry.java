package com.enquiry.domain.enquiry;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import com.enquiry.domain.common.DomainModel;
import com.enquiry.domain.common.InvalidPropertyException;
import com.enquiry.domain.enquiry.EnquiryConstants.CURRENCY;
import com.enquiry.domain.enquiry.EnquiryConstants.ENQUIRY_EVENTS;
import com.enquiry.domain.enquiry.entities.OwnerUpdate;
import com.enquiry.domain.enquiry.valueobjects.Price;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Synchronized;

@Data
@EqualsAndHashCode(callSuper = false , onlyExplicitlyIncluded = true)
public class Enquiry extends DomainModel<ENQUIRY_EVENTS>{
	private static final long serialVersionUID = -7684457618631108033L;
	private Enquiry() {}
	
	private String userID;
	/**
	 * Using EnquiryUpdate instead of EnquiryUpdateID as EnquiryUpdate is only used
	 * in this Aggregate and race conditions should not occur
	 */
	private List<OwnerUpdate> ownerUpdates = Collections.emptyList();
	private String apartmentID;
	private Price requestedPrice;
	private String tenentDescription;
	private int totalPersons;
	private String relationship;
	private boolean isCompleted;
	
	/** add business logic like this to make rich models instead of enemic ones **/
	public final boolean updateCompletedStatus() {
		this.isCompleted = !ownerUpdates.isEmpty() && ownerUpdates.get(ownerUpdates.size() - 1).ownerAccepted();
		return this.isCompleted;
	}

	public static final OwnerUpdate createOwnerUpdate(String enquiryId , Price requestedPrice, String status , String remarks) {
		return OwnerUpdate.create(enquiryId, requestedPrice, status , remarks);
	}
	
	/**
	 * To support internationalization please use separate exception for each 
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
	 */
	@Synchronized
	public static final Enquiry createEnquiry(final String userID , final String apartmentID , final String relationShip , 
			final String tenantDescription , final long requestedPrice , final String currency , final int totalPersons) {
		if(totalPersons > 1 && 
				!EnquiryConstants.RELATIONSHIPS.BACHLEOUR.name().equals(relationShip)) {
			throw new InvalidPropertyException("Relationship cannot be other than bachleour if total persons are 1");
		}
		Enquiry enquiry = new Enquiry();
		enquiry.setId(UUID.randomUUID().toString());
		enquiry.setUserID(userID);
		enquiry.setApartmentID(apartmentID);
		enquiry.setRelationship(relationShip);
		enquiry.setTenentDescription(tenantDescription);
		enquiry.setRequestedPrice(Price.create(requestedPrice , currency));
		enquiry.setTotalPersons(totalPersons);
		enquiry.addDomainEvents(ENQUIRY_EVENTS.NEW_ENQUIRY);
		
		return enquiry;
	}
	
	public static final Enquiry getEmptyInstance() { return new Enquiry();}
	
	public void updateEnquiry(final long requestedPrice, final String currency,
			final String tenentDescription, final int totalPersons, final String relationship) {
		CURRENCY requestedCurrency = CURRENCY.valueOf(currency);
		if(!requestedCurrency.equals(this.requestedPrice.getCurrency())) {
			 this.requestedPrice.convertCurrency(this.requestedPrice.getCurrency(), requestedCurrency);
		}else {
			this.requestedPrice.setAmount(requestedPrice);
		}
		
		this.tenentDescription = tenentDescription;
		this.totalPersons = totalPersons;
		this.relationship = relationship;
		this.domainEvents.getEventNames().add(ENQUIRY_EVENTS.ENQUIRY_UPDATED);
	}
}
