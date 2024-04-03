package com.packt.hibernate.multitenant;

import java.sql.Connection;
import java.sql.SQLException;

import org.hibernate.engine.jdbc.connections.spi.MultiTenantConnectionProvider;


public class ConnectionProviderImpl implements MultiTenantConnectionProvider {

	@Override
	public Connection getAnyConnection() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void releaseAnyConnection(Connection connection) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Connection getConnection(String tenantIdentifier)
			throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void releaseConnection(String tenantIdentifier, Connection connection)
			throws SQLException {
	}

	@Override
	public boolean isUnwrappableAs(Class unwrapType) {
		return false;
	}
	
	@Override
	public <T> T unwrap(Class<T> unwrapType) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean supportsAggressiveRelease() {
		return true;
	}

	

}
