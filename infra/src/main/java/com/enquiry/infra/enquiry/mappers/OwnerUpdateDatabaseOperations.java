package com.enquiry.infra.enquiry.mappers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.enquiry.domain.enquiry.entities.OwnerUpdate;
import com.enquiry.domain.enquiry.valueobjects.Price;
import com.enquiry.infra.common.DatabaseException;
import com.enquiry.infra.common.IDatabaseOperations;

import io.r2dbc.spi.Row;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class OwnerUpdateDatabaseOperations implements IDatabaseOperations<OwnerUpdate>{
	
	@Override
	public OwnerUpdate map(Row row) {
		try {
			return new OwnerUpdate(row.get("id", String.class),
					row.get("enquiryID", String.class),
					Price.create(row.get("negotiatedPrice", Long.class), row.get("currency", String.class)),
					row.get("status", String.class),
					row.get("remarks", String.class));
		}catch (Exception e) {
			log.error("Error while mapping {} " , e);
			throw new DatabaseException(e.getMessage());
		}

	}

	@Override
	public Map<String, Object> createBindings(OwnerUpdate e) {
		Map<String , Object> bindings = new HashMap<>();
		
		bindings.put("id", e.getId());
		bindings.put("enquiryID", e.getEnquiryID());
		bindings.put("negotiatedPrice", e.getNegotiatedPrice().getAmount());
		bindings.put("currency", e.getNegotiatedPrice().getCurrency().toString());
		bindings.put("status", e.getStatus());
		bindings.put("remarks", e.getRemarks());
		
		return bindings;
	}

	@Override
	public String createInsertQuery() {
		return "insert into owner_updates values(:id, :enquiryID, :negotiatedPrice, :currency, :status , :remarks)";
	}
	
	@Override
	public String createUpdateQuery() {
		return """
				update enquiries set  enquiryID = :enquiryID, negotiatedPrice = :negotiatedPrice , status = :status, remarks = :remarks \
				where id = :id """;
	}
	
	public static final String SCHEMA = """
			CREATE TABLE IF NOT EXISTS enquiryupdates ( \
				id varchar(100) PRIMARY KEY, \
				enquiryID varchar(100) NOT NULL, \
				negotiatedPrice long NULL, \
				status varchar(100) NULL, \
				remarks varchar(100) NULL) """;
	
	public static final String FIND_BY_ENQID = "select * from "+OwnerUpdate.TABLE_NAME+" where enquiryID = :enquiryID";
}
