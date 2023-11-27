package com.enquiry.infra.enquiry.mappers;

import com.enquiry.domain.enquiry.Enquiry;
import com.enquiry.domain.enquiry.entities.OwnerUpdate;
import com.enquiry.domain.enquiry.valueobjects.Price;

import io.r2dbc.spi.Row;

public final class EnquiryRowMapper {
	private EnquiryRowMapper() {}
	
	public static final Enquiry map(Row row) {
		Enquiry enquiry = Enquiry.getEmptyInstance();

		enquiry.setId(row.get("id", String.class));
		enquiry.setUserID(row.get("userID", String.class));
		enquiry.setApartmentID(row.get("apartmentID", String.class));
		enquiry.setRequestedPrice(
				Price.create(row.get("requestedPrice", Long.class), row.get("currency", String.class)));
		enquiry.setTenentDescription(row.get("tenantDescription", String.class));
		enquiry.setTotalPersons(row.get("totalPersons", Integer.class));
		enquiry.setRelationship(row.get("relationship", String.class));
		return enquiry;
	}
	
	public static final OwnerUpdate mapOwnerUpdates(Row row) {
		OwnerUpdate ownerUpdate = OwnerUpdate.getEmptyInstance();

		ownerUpdate.setId(row.get("id", String.class));
		ownerUpdate.setEnquiryId(row.get("enquiryId", String.class));
		ownerUpdate.setNegotiatedPrice(Price.create(row.get("negotiatedPrice", Long.class), row.get("currency", String.class)));
		ownerUpdate.setStatus(row.get("status", String.class));
		ownerUpdate.setRemarks(row.get("remarks", String.class));
		return ownerUpdate;
	}
}
