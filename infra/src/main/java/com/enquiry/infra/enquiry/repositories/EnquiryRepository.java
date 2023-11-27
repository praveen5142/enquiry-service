package com.enquiry.infra.enquiry.repositories;

import org.springframework.data.r2dbc.convert.MappingR2dbcConverter;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;

import com.enquiry.domain.enquiry.Enquiry;
import com.enquiry.domain.enquiry.entities.OwnerUpdate;
import com.enquiry.domain.enquiry.repositories.IEnquiryRepository;
import com.enquiry.infra.enquiry.mappers.EnquiryRowMapper;

import lombok.AllArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
@AllArgsConstructor
public class EnquiryRepository implements IEnquiryRepository {
	private final DatabaseClient databaseClient;
	private final MappingR2dbcConverter converter;
	
	private static final String SAVE_SQL = """
			insert into enquiries values(:id, :userID, :apartmentID, :requestedPrice, :currency , :tenantDescription, \
			:totalPersons, :relationship, :isCompleted) """;

	@Override
	public Mono<Enquiry> save(Enquiry enquiry) {
		return databaseClient.sql(SAVE_SQL)
	            .bind("id", enquiry.getId())
	            .bind("userID", enquiry.getUserID())
	            .bind("apartmentID", enquiry.getApartmentID())
	            .bind("requestedPrice", enquiry.getRequestedPrice().getAmount())
	            .bind("currency", enquiry.getRequestedPrice().getCurrency().name())
	            .bind("tenantDescription", enquiry.getTenentDescription())
	            .bind("totalPersons", enquiry.getTotalPersons())
	            .bind("relationship", enquiry.getRelationship())
	            .bind("isCompleted", enquiry.isCompleted())
	            .fetch().rowsUpdated()
	            .switchIfEmpty(Mono.error(new RuntimeException("Unable to insert enquiry, check logs")))
	            .map(value -> enquiry);
	}
	
	private static final String UPDATE_SQL = """
			update enquiries set  requestedPrice = :requestedPrice, currency = :currency , tenentDescription = :tenantDescription, \
			totalPersons = :totalPersons, relationship = :relationship, isCompleted = :isCompleted) """;
	
	@Override
	public Mono<Enquiry> update(Enquiry enquiry) {
		return databaseClient.sql(UPDATE_SQL)
	            .bind("requestedPrice", enquiry.getRequestedPrice().getAmount())
	            .bind("currency", enquiry.getRequestedPrice().getCurrency())
	            .bind("tenantDescription", enquiry.getTenentDescription())
	            .bind("totalPersons", enquiry.getTotalPersons())
	            .bind("relationship", enquiry.getRelationship())
	            .bind("isCompleted", enquiry.isCompleted())
	            .fetch().rowsUpdated()
	            .switchIfEmpty(Mono.error(new RuntimeException("Enquiry not found, check logs")))
	            .map(value -> enquiry);
	}

	private static final String FETCH_ALL_ENQ = "select enq.* from enquiries enq";
	
	@Override
	public Flux<Enquiry> findAll() {
		return databaseClient.sql(FETCH_ALL_ENQ)
				.map((row, metaData) -> EnquiryRowMapper.map(row))
				.all()
				.map(enquiry-> { 
	            	findAllOwnerUpdates(enquiry.getId())
	            	.collectList()
                    .subscribe(enquiry::setOwnerUpdates);
	            	return enquiry;
	            });
	}
	
	private static final String FETCH_ALL_BY_APARTMENT = "select enq.* from enquiries enq where apartmentID = :apartmentID";
	
	@Override
	public Flux<Enquiry> findAllByApartmentId(String id) {
		return databaseClient.sql(FETCH_ALL_BY_APARTMENT)
				.bind("apartmentID", id)
				.map((row, metaData) -> converter.read(Enquiry.class, row))
				.all()
				.map(enquiry-> { 
	            	findAllOwnerUpdates(id)
	            	.collectList()
                    .subscribe(enquiry::setOwnerUpdates);
	            	return enquiry;
	            });
	}
	
	private static final String FETCH_BY_ID = "select enq.* from enquiries enq where id = :id";
	
	@Override
	public Mono<Enquiry> findById(String id) {
		return databaseClient.sql(FETCH_BY_ID)
	            .bind("id", id)
	            .map((row, metadata) -> EnquiryRowMapper.map(row))
	            .one()
	            .map(enquiry-> { 
	            	findAllOwnerUpdates(id)
	            	.collectList()
                    .subscribe(enquiry::setOwnerUpdates);
	            	return enquiry;
	            });
	}
	
	 public Mono<Boolean> isEnquiryAvailable(String id) {
        return databaseClient.sql("SELECT COUNT(*) FROM enquiries WHERE id = :id")
            .bind("id", id)
            .map((row, metadata) -> row.get(0, Long.class) > 0)
            .first();
	}
	 
	private static final String ADD_OWNER_UPDATE = "insert into owner_updates values(:id, :enquiryId, :negotiatedPrice, :requestedPrice, :status , :remarks)";
		
	@Override
	public Mono<Enquiry> addOwnerUpdate(OwnerUpdate ownerUpdate) {
		return isEnquiryAvailable(ownerUpdate.getEnquiryId())
				.flatMap(enquiryExists -> {
					if (enquiryExists) {
						return databaseClient.sql(ADD_OWNER_UPDATE)
		            		.bind("id", ownerUpdate.getId())
							.bind("enquiryId", ownerUpdate.getEnquiryId())
							.bind("negotiatedPrice", ownerUpdate.getNegotiatedPrice().getAmount())
							.bind("status", ownerUpdate.getStatus())
							.bind("remarks", ownerUpdate.getRemarks())
		                    .fetch()
		                    .rowsUpdated()
		                    .then()
		                    .then(findById(ownerUpdate.getEnquiryId()));
		            } else {
		                return Mono.error(new RuntimeException("Enquiry not found"));
		            }
				});
	}

	private static final String DELETE_BY_ID = "delete from enquiries where id = :id";
	private static final String DELETE_UPDATES_BY_ID = "delete from owner_updates where enquiryId = :enquiryId";
	
	@Override
	public Mono<Long> delete(String id) {
		return databaseClient.sql(DELETE_UPDATES_BY_ID)
				.bind("enquiryId", id)
				.then()
				.then(this.databaseClient.sql(DELETE_BY_ID)
					.bind("id", id)
					.fetch().rowsUpdated()
		            .switchIfEmpty(Mono.error(new RuntimeException("Unable to delete enquiry, check logs"))));
	}

	private static final String FETCH_ALL_UPD = "select upd.* from owner_updates where upd.enquiryId = :enquiryId";
	
	private Flux<OwnerUpdate> findAllOwnerUpdates(String enquiryId) {
        return databaseClient.sql(FETCH_ALL_UPD)
            .bind("enquiryId", enquiryId)
            .map((row, metadata) -> EnquiryRowMapper.mapOwnerUpdates(row))
            .all();
    }

	private static final String CREATE_ENQUIRIES_SCHEMA = """
					CREATE TABLE IF NOT EXISTS enquiries ( \
						id varchar(100) PRIMARY KEY, \
						apartmentID varchar(100) NOT NULL, \
						userID varchar(100) NOT NULL, \
						requestedPrice long NULL, \
						currency varchar(100) NULL, \
						totalPersons long NOT NULL, \
						enquiryStatus varchar(100) NULL, \
						notificationStatus varchar(100) NULL, \
						tenentDescription varchar(100) NULL, \
						relationship varchar(100) NULL ) """;
	
	private static final String CREATE_OWNER_UPDATES_SCHEMA = """
					CREATE TABLE IF NOT EXISTS enquiryupdates ( \
						id varchar(100) PRIMARY KEY, \
						enquiryId varchar(100) NOT NULL, \
						negotiatedPrice long NULL, \
						status varchar(100) NULL, \
						remarks varchar(100) NULL) """;
	
	@Override
	public Mono<Void> createSchema() {
		return databaseClient.sql(CREATE_ENQUIRIES_SCHEMA)
				.then().log("Enquiries created!")
				.then(databaseClient.sql(CREATE_OWNER_UPDATES_SCHEMA).then().log("Owner update created!"));
	}
}