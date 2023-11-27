package com.enquiry.contracts.enquiry;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
class Price implements Serializable {
	private static final long serialVersionUID = 1L;

	private long amount;
	private String currency;
}

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EnquiryResponse implements Serializable{
	private static final long serialVersionUID = -8316481290617576842L;
	private String id;
	private String userID;
	private List<OwnerUpdateResponse> ownerUpdates;
	private String apartmentID;
	private Price requestedPrice;
	private String tenentDescription;
	private int totalPersons;
	private String relationship;
	private boolean isCompleted;
}
