package com.enquiry.domain.enquiry.repositories;

import com.enquiry.domain.common.IBaseRepository;
import com.enquiry.domain.enquiry.entities.OwnerUpdate;

import reactor.core.publisher.Flux;

public interface IOwnerUpdateRepository extends IBaseRepository<OwnerUpdate , String>{
	Flux<OwnerUpdate> findAllByEnquiryID(String enquiryID);
}