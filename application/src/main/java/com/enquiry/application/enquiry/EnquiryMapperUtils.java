package com.enquiry.application.enquiry;

import org.springframework.beans.BeanUtils;

import com.enquiry.contracts.enquiry.CreateEnquiryRequest;
import com.enquiry.contracts.enquiry.EnquiryResponse;
import com.enquiry.contracts.enquiry.EnquiryUpdateRequest;
import com.enquiry.contracts.enquiry.OwnerUpdateRequest;
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
			BeanUtils.copyProperties(dto, enquiry);
			return dto;
		} catch (Exception e) {
			log.error("{}", e);
			return null;
		}
	}

	public static final Enquiry createEnquiryFromReq(CreateEnquiryRequest request) {
		/* fetch location info from request/jwt token details and set currency accordingly */
		return Enquiry.createEnquiry(request.getUserID(), request.getApartmentID(), request.getRelationship(),
				request.getTenentDescription(), request.getRequestedPrice(), request.getCurrency(), request.getTotalPersons());
	}
	
	public static final Enquiry createEnquiryUpdateFromReq(EnquiryUpdateRequest request) {
		/* fetch location info from request/jwt token details and set currency accordingly */
		Enquiry enquiry = Enquiry.getEmptyInstance();
		enquiry.setId(request.getId());
		enquiry.setRequestedPrice(Price.create(request.getRequestedPrice(), request.getCurrency()));
		enquiry.setTenentDescription(request.getTenentDescription());
		enquiry.setTotalPersons(enquiry.getTotalPersons());
		enquiry.setRelationship(enquiry.getRelationship());
		
		return enquiry;
	}
	
	public static final OwnerUpdate createOwnerUpdateFromReq(OwnerUpdateRequest request) {
		return Enquiry.createOwnerUpdate(request.getEnquiryID() , Price.create(request.getNegotiatedPrice().getAmount(),
				request.getNegotiatedPrice().getCurrency()), request.getStatus(), request.getRemarks());
	}
}
