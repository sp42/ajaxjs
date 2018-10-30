package com.ajaxjs.orm;

import java.sql.ResultSet;
import java.sql.SQLException;

@FunctionalInterface
public interface ResultSetProcessor<T> {
	/**
	 * 
	 * @param resultSet
	 * @param currentRow
	 * @throws SQLException
	 */
	public T process(ResultSet resultSet) throws SQLException;
}