package com.enquiry.contracts.enquiry;

import java.io.Serializable;

import com.enquiry.contracts.common.PriceDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OwnerUpdateResponse implements Serializable{
	private static final long serialVersionUID = -8316481290617576842L;
	
	private String enquiryID;
	private PriceDTO negotiatedPrice;
	private String status;
	private String remarks;
}
