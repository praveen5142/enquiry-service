package com.enquiry.infra.enquiry.mappers;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.enquiry.domain.enquiry.Enquiry;
import com.enquiry.domain.enquiry.valueobjects.Price;
import com.enquiry.infra.common.DatabaseException;
import com.enquiry.infra.common.IDatabaseOperations;

import io.r2dbc.spi.Row;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class EnquiryDatabaseOperations implements IDatabaseOperations<Enquiry>{

	public Enquiry map(Row row) {
		try {
			return new Enquiry(row.get("id", String.class), row.get("userID", String.class), null,
					row.get("apartmentID", String.class),
					Price.create(row.get("requestedPrice", Long.class), row.get("currency", String.class)),
					row.get("tenentDescription", String.class), row.get("totalPersons", Long.class),
					row.get("relationship", String.class) , row.get("lastUpdated" , LocalDateTime.class));
		}catch (Exception e) {
			log.error("Error while mapping {} " , e);
			throw new DatabaseException(e.getMessage());
		}
	}

	@Override
	public Map<String, Object> createBindings(Enquiry e) {
		Map<String , Object> bindings = new HashMap<>();
		
		bindings.put("id", e.getId());
		bindings.put("apartmentID", e.getApartmentID());
		bindings.put("userID", e.getUserID());
		bindings.put("requestedPrice", e.getRequestedPrice().getAmount());
		bindings.put("currency", e.getRequestedPrice().getCurrency().toString());
		bindings.put("totalPersons", e.getTotalPersons());
		bindings.put("tenentDescription", e.getTenentDescription());
		bindings.put("relationship", e.getRelationship());
		bindings.put("lastUpdated", e.getLastUpdated());
		
		return bindings;
	}

	@Override
	public String createInsertQuery() {
		return "insert into enquiries values(:id, :apartmentID, :userID, :requestedPrice, :currency , :totalPersons, :tenentDescription, :relationship)";
	}

	@Override
	public String createUpdateQuery() {
		return """
				update enquiries set  requestedPrice = :requestedPrice, currency = :currency , tenentDescription = :tenantDescription, \
				totalPersons = :totalPersons, relationship = :relationship where id = :id """;
	}
	
	public static final String SCHEMA = """
			CREATE TABLE IF NOT EXISTS enquiries ( \
				id varchar(100) PRIMARY KEY, \
				apartmentID varchar(100) NOT NULL, \
				userID varchar(100) NOT NULL, \
				requestedPrice long NULL, \
				currency varchar(100) NULL, \
				totalPersons long NOT NULL, \
				tenentDescription varchar(100) NULL, \
				relationship varchar(100) NULL ) """;
	
	public static final String FETCH_ALL_BY_APARTMENT = "select enq.* from enquiries enq where apartmentID = :apartmentID";

}
