package com.service;

import java.sql.Connection;

/**
 * Interface for connecting to the H2 DB
 * 
 * @author colingray
 *
 */
public interface IDBConnectionManager {
	public Connection getConnection();
}
