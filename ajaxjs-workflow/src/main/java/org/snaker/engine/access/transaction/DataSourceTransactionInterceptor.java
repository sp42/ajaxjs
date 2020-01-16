/*
 * Copyright 2013-2015 www.snakerflow.com. Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law
 * or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package org.snaker.engine.access.transaction;

import java.sql.Connection;

import javax.sql.DataSource;

import org.snaker.engine.access.JdbcHelper;

import com.ajaxjs.util.logger.LogHelper;

/**
 * Jdbc方式的数据库事务拦截处理
 * 
 * @author yuqs
 * @since 1.0
 */
public class DataSourceTransactionInterceptor extends TransactionInterceptor {
	public static final LogHelper LOGGER = LogHelper.getLog(DataSourceTransactionInterceptor.class);

	private DataSource dataSource;

	public void initialize(Object accessObject) {
		if (accessObject == null)
			return;

		if (accessObject instanceof DataSource) {
			this.dataSource = (DataSource) accessObject;
		}
	}

	protected TransactionStatus getTransaction() {
		try {
			boolean isExistingTransaction = TransactionObjectHolder.isExistingTransaction();
			if (isExistingTransaction)
				return new TransactionStatus(TransactionObjectHolder.get(), false);

			Connection conn = JdbcHelper.getConnection(dataSource);
			conn.setAutoCommit(false);
			LOGGER.info("begin transaction=" + conn.hashCode());

			TransactionObjectHolder.bind(conn);
			return new TransactionStatus(conn, true);
		} catch (Exception e) {
			LOGGER.warning(e.getMessage(), e);
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	protected void commit(TransactionStatus status) {
		if (!status.isNewTransaction())
			throw new RuntimeException("It should be TRUE!");

		try (Connection conn = (Connection) status.getTransaction();) {
			LOGGER.info("commit transaction=" + conn.hashCode());
			conn.commit();
		} catch (Exception e) {
			LOGGER.warning(e);
			throw new RuntimeException(e.getMessage(), e);
		} finally {
			TransactionObjectHolder.unbind();
		}
	}

	protected void rollback(TransactionStatus status) {
		try (Connection conn = (Connection) status.getTransaction();) {
			LOGGER.info("rollback transaction=" + conn.hashCode());
			if (!conn.isClosed())
				conn.rollback();
		} catch (Exception e) {
			LOGGER.warning(e);
			throw new RuntimeException(e.getMessage(), e.getCause());
		} finally {
			TransactionObjectHolder.unbind();
		}
	}

}
