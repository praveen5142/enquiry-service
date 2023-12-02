package com.enquiry.application.enquiry;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.enquiry.contracts.enquiry.EnquiryRequest;
import com.enquiry.contracts.enquiry.EnquiryResponse;
import com.enquiry.contracts.enquiry.EnquiryUpdateRequest;
import com.enquiry.contracts.enquiry.OwnerUpdateRequest;
import com.enquiry.domain.common.exceptions.DomainException;
import com.enquiry.domain.enquiry.services.IEnquiryCommand;
import com.enquiry.domain.enquiry.services.IEnquiryQuery;

import lombok.AllArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(EnquiryController.BASE_URL)
@AllArgsConstructor
public class EnquiryController {
	public static final String BASE_URL = "user/enquiries";
	public static final String ENQUIRY_RESP_ERROR = "Error creating enquiry";
	public static final String ENQUIRY_NOT_FOUND = "Enquiry not Found";
	
	private final IEnquiryCommand enquiryCommand;
	private final IEnquiryQuery enquiryQuery;
	
	@GetMapping
	public Flux<EnquiryResponse> getAll(){
		return enquiryQuery.getAll().map(EnquiryMapperUtils::createEnquiryResponse);
	}
	
	@GetMapping("/{id}")
	public Mono<EnquiryResponse> getEnquiry(@PathVariable String id){
		return enquiryQuery.getEnquiry(id).map(EnquiryMapperUtils::createEnquiryResponse);
	}
	
	@GetMapping("/getAllByAparmentId/{apartmentId}")
	public Flux<EnquiryResponse> getAllByAparmentId(@PathVariable String apartmentId){
		return enquiryQuery.getAllByAparmentId(apartmentId).map(EnquiryMapperUtils::createEnquiryResponse);
	}
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
    public Mono<EnquiryResponse> create(@RequestBody EnquiryRequest request) {
		try {
			return enquiryCommand.createEnquiry(EnquiryMapperUtils.createEnquiryFromReq(request))
					.map(EnquiryMapperUtils::createEnquiryResponse)
					.switchIfEmpty(Mono.error(new RuntimeException(ENQUIRY_RESP_ERROR)));
		}catch (DomainException e) {
			return Mono.error(e);
		}
	}
	
	@PutMapping
    public Mono<EnquiryResponse> update(@RequestBody EnquiryUpdateRequest request) {
		try {
			return enquiryCommand.updateEnquiry(EnquiryMapperUtils.createEnquiryUpdateFromReq(request))
					.map(EnquiryMapperUtils::createEnquiryResponse)
					.switchIfEmpty(Mono.error(new RuntimeException(ENQUIRY_NOT_FOUND)));
		}catch (DomainException e) {
			return Mono.error(e);
		}
	}
	
	@PostMapping("/ownerUpdate")
	@ResponseStatus(HttpStatus.CREATED)
    public Mono<EnquiryResponse> ownerUpdate(@RequestBody OwnerUpdateRequest request) {
		try {
			return enquiryCommand.ownerUpdate(EnquiryMapperUtils.createOwnerUpdateFromReq(request))
					.map(EnquiryMapperUtils::createEnquiryResponse)
					.switchIfEmpty(Mono.error(new RuntimeException(ENQUIRY_NOT_FOUND)));
			
		}catch (DomainException e) {
			return Mono.error(e);
		}
	}
}
