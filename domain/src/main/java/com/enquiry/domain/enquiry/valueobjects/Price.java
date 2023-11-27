package com.enquiry.domain.enquiry.valueobjects;

import java.io.Serializable;

import com.enquiry.domain.enquiry.EnquiryConstants.CURRENCY;

import lombok.Data;

@Data
public class Price implements Serializable {
	private Price() {}
	private static final long serialVersionUID = 1L;

	private long amount;
	private CURRENCY currency;

	/* enterprise applications will not use it definately*/
	public void convertCurrency(CURRENCY from, CURRENCY to) {
		if (CURRENCY.USD.equals(from) && CURRENCY.INR.equals(to)) {
			this.amount = this.amount * 80L;
		} else if (CURRENCY.INR.equals(from) && CURRENCY.USD.equals(to)) {
			if (this.amount != 0)
				this.amount = this.amount / 80;
		}
		
		this.currency = to;
	}
	
	public static final Price create(final long amount , final String currency) {
		Price price = new Price();
		price.setAmount(amount);
		price.setCurrency(CURRENCY.valueOf(currency));
		return price;
	}
}
