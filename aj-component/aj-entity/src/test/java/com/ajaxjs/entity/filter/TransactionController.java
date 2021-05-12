package com.ajaxjs.entity.filter;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

import com.ajaxjs.sql.JdbcConnection;
import com.ajaxjs.sql.JdbcHelper;
import com.ajaxjs.web.mvc.IController;
import com.ajaxjs.web.mvc.filter.MvcFilter;

@Path("/foo")
public class TransactionController implements IController {
	@GET
	@EnableTransaction
	@MvcFilter(filters = { DataBaseFilter.class })
	public String foo1() {
		JdbcHelper.create(JdbcConnection.getConnection(), "INSERT INTO TransactionTest (name) VALUES (?)", "test1");

		return "html::Foo";
	}

	@SuppressWarnings("unused")
	@GET
	@EnableTransaction
	@Path("bar")
	@MvcFilter(filters = { DataBaseFilter.class })
	public String foo2() {
		JdbcHelper.create(JdbcConnection.getConnection(), "INSERT INTO TransactionTest (name) VALUES (?)", "test2");
		
		if (true) {
			throw new Error("Err");
		}

		JdbcHelper.create(JdbcConnection.getConnection(), "INSERT INTO TransactionTest (name) VALUES (?)", "test3");

		return "html::Foo";
	}
}
