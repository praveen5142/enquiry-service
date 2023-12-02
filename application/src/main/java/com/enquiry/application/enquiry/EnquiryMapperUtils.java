package com.enquiry.application.enquiry;

import org.springframework.beans.BeanUtils;

import com.enquiry.contracts.enquiry.EnquiryRequest;
import com.enquiry.contracts.enquiry.EnquiryResponse;
import com.enquiry.contracts.enquiry.EnquiryUpdateRequest;
import com.enquiry.contracts.enquiry.OwnerUpdateRequest;
import com.enquiry.domain.common.exceptions.DomainException;
import com.enquiry.domain.enquiry.Enquiry;
import com.enquiry.domain.enquiry.entities.OwnerUpdate;
import com.enquiry.domain.enquiry.valueobjects.Price;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class EnquiryMapperUtils {
	private EnquiryMapperUtils() {}
	
	public static final EnquiryResponse createEnquiryResponse(Enquiry enquiry) {
		EnquiryResponse dto = new EnquiryResponse();
		try {
			BeanUtils.copyProperties(enquiry , dto);
			return dto;
		} catch (Exception e) {
			log.error("{}", e);
			return null;
		}
	}

	public static final Enquiry createEnquiryFromReq(EnquiryRequest request) throws DomainException{
		/* fetch location info from request/jwt token details and set currency accordingly */
			return new Enquiry(request.getUserID() , null , request.getApartmentID(), Price.create(request.getRequestedPrice(), request.getCurrency()) ,
					request.getTenentDescription(),  request.getTotalPersons() , request.getRelationship());
	}
	
	public static final Enquiry createEnquiryUpdateFromReq(EnquiryUpdateRequest request) throws DomainException {
		/* fetch location info from request/jwt token details and set currency accordingly */
		return new Enquiry(request.getId() , null , null , null , Price.create(request.getRequestedPrice(), request.getCurrency()) ,
				request.getTenentDescription(),  request.getTotalPersons() , request.getRelationship());
	}
	
	public static final OwnerUpdate createOwnerUpdateFromReq(OwnerUpdateRequest request) throws DomainException {
		return Enquiry.createOwnerUpdate(request.getEnquiryID() , Price.create(request.getNegotiatedPrice().getAmount(),
				request.getNegotiatedPrice().getCurrency()), request.getStatus(), request.getRemarks());
	}
}
