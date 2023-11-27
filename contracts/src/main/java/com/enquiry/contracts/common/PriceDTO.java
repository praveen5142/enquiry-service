package com.enquiry.contracts.common;

import java.io.Serializable;

import lombok.Data;

@Data
public class PriceDTO implements Serializable {
	private static final long serialVersionUID = 1L;

	private long amount;
	private String currency;
}