package com.enquiry.infra.common;

import org.springframework.r2dbc.core.DatabaseClient;

import com.enquiry.domain.common.DomainModel;
import com.enquiry.domain.common.IBaseRepository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * BaseRepository implementation for basic CRUD operations.
 * Note this class uses reflection API but we have cached fields data into @metaDataMap object 
 * for no reflection throughput. But if in case any database release is done , please make sure to restart application or 
 * create endpoint for @metaDataMap cache update.
 * 
 * @author prakumar113
 *
 * @param <E>
 * @param <T>
 */
@AllArgsConstructor
@Slf4j
public abstract class BaseRepositoryImpl<E extends DomainModel<?>, T> implements IBaseRepository<E, T> {
	private final DatabaseClient databaseClient;
	private final String tableName;
	private final String schemaQuery;
	private final IDatabaseOperations<E> databaseOperations;
	private static final String SSF = "select * from ";
	
	@Override
	public Mono<E> save(E e) {
		if (e == null)
			return Mono.just(null);
		try {
			
			return databaseClient.sql(databaseOperations.createInsertQuery()).bindValues(databaseOperations.createBindings(e))
					.fetch().rowsUpdated()
					.switchIfEmpty(Mono.error(new RuntimeException("Unable to insert into "+e.getTableName()+", check logs")))
					.map(value -> e);

		} catch (Exception exp) {
			log.error("Error save , {}" , exp);
			return Mono.error(new DatabaseException(exp.getMessage()));
		}
	}
	
	@Override
	public Mono<E> update(E e) {
		if (e == null)
			return Mono.just(null);
		try {
			return databaseClient.sql(databaseOperations.createUpdateQuery()).bindValues(databaseOperations.createBindings(e))
					.fetch().rowsUpdated()
					.switchIfEmpty(Mono.error(new RuntimeException("Unable to insert into "+e.getTableName()+", check logs")))
					.map(value -> e);

		} catch (Exception exp) {
			log.error("Error save , {}" , exp);
			return Mono.error(new DatabaseException(exp.getMessage()));
		}
	}

	@Override
	public Flux<E> findAll() {
		return databaseClient.sql(SSF + this.tableName).map((row, metadata) -> databaseOperations.map(row)).all();
	}

	@Override
	public Mono<E> findById(T id) {
		return databaseClient.sql(SSF + this.tableName + " where id = :id")
				.bind("id", id)
				.map((row, metadata) -> databaseOperations.map(row))
				.one();
	}

	@Override
	public Mono<Long> delete(T id) {
		return databaseClient.sql("delete from " + this.tableName + " where id = :id").bind("id", id)
				.fetch().rowsUpdated();
	}

	public Mono<Boolean> available(T id) {
        return databaseClient.sql("SELECT COUNT(*) FROM enquiries WHERE id = :id")
            .bind("id", id)
            .map((row, metadata) -> row.get(0, Long.class) > 0)
            .first();
	}
	
	@Override
	public Mono<Void> createSchema() {
		return databaseClient.sql(schemaQuery).then();
	}
}
