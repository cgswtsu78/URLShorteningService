package com.service.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.service.IDBConnectionManager;

/**
 * Initializes the DB connection
 * 
 * @author colingray
 *
 */
public class DBConnectionManager implements IDBConnectionManager {
	final static Logger logger = Logger.getLogger(DBConnectionManager.class);
	private static final String DB_DRIVER = "org.h2.Driver";

	private Connection connection;

	public DBConnectionManager(String dbURL, String user, String pwd) {
		logger.info("Initializing DB Connection");
		try {
			Class.forName(DB_DRIVER);
		} catch (ClassNotFoundException e) {
			logger.error("ClassNotFoundException occurred while initializing the DB: " + e.getMessage());
		}
		try {
			this.connection = DriverManager.getConnection(dbURL, user, pwd);
		} catch (SQLException e) {
			logger.error("SQLException occurred while initializing the DB: " + e.getMessage());
		}

	}

	/**
	 * returns the DB connection
	 */
	public Connection getConnection() {
		return this.connection;
	}
}