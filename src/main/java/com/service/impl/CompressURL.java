package com.service.impl;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.service.ICompressURL;

/**
 * Responsible for the business rules for compressing a URL into a shortened URL
 * 
 * @author colingray
 *
 */
public class CompressURL implements ICompressURL {
	final static Logger logger = Logger.getLogger(CompressURL.class);

	/**
	 * adds the long URL to the database if its valid
	 */
	public String addURL(Connection connection, String longURL) throws SQLException {
		if (!isURLValid(longURL)) {
			return null;
		}
		longURL = longURL.trim();
		if (longURL.endsWith("/")) {
			longURL = longURL.substring(0, longURL.lastIndexOf("/"));
		}
		PreparedStatement selectPreparedStatement = null;
		ResultSet selectRS = null;
		String id = null;
		selectPreparedStatement = connection.prepareStatement("SELECT * FROM URL WHERE LONG_URL=?");
		selectPreparedStatement.setString(1, longURL);
		selectRS = selectPreparedStatement.executeQuery();

		if (selectRS != null && selectRS.next()) {
			id = Integer.toString(selectRS.getInt("ID"));
			logger.info("URL already in use, return existing ID:" + id);
		} else {
			logger.info("Inserting new URL:" + longURL);
			id = insertURL(connection, longURL);
		}
		return id;
	}

	/**
	 * attempts to connect to the URL to determine its validity
	 */
	public boolean isURLValid(String longURL) {
		boolean isValid = false;
		try {
			URL url = new URL(longURL);
			URLConnection conn = url.openConnection();
			conn.connect();
			isValid = true;
		} catch (MalformedURLException e) {
			logger.info("Invalid URL:" + longURL + e.getMessage());
		} catch (IOException e) {
			logger.info("Invalid URL:" + longURL + e.getMessage());
		}
		return isValid;
	}

	/**
	 * inserts the URL and returns the DB ID to use as the shortened URL key
	 * 
	 * @param connection
	 * @param longURL
	 * @return String
	 * @throws SQLException
	 */
	private String insertURL(Connection connection, String longURL) throws SQLException {
		String id = null;
		PreparedStatement insertPreparedStatement = null;
		PreparedStatement selectPreparedStatement = null;
		String InsertQuery = "INSERT INTO URL" + "(LONG_URL) values" + "(?)";
		String SelectQuery = "SELECT * FROM URL WHERE LONG_URL='" + longURL + "'";

		connection.setAutoCommit(false);
		insertPreparedStatement = connection.prepareStatement(InsertQuery);
		insertPreparedStatement.setString(1, longURL);
		insertPreparedStatement.executeUpdate();
		insertPreparedStatement.close();

		selectPreparedStatement = connection.prepareStatement(SelectQuery);
		ResultSet rs = selectPreparedStatement.executeQuery();

		while (rs.next()) {
			id = Integer.toString(rs.getInt("ID"));
		}
		selectPreparedStatement.close();
		connection.commit();
		logger.info("New URL: " + longURL + " Inserted");
		return id;
	}
}
