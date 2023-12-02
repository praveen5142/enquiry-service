package com.enquiry.infra.common;

import java.util.Map;

import io.r2dbc.spi.Row;

public interface IDatabaseOperations<E>{
	public E map(Row row);
	public Map<String , Object> createBindings(E e);
	public String createInsertQuery();
	public String createUpdateQuery();
}
