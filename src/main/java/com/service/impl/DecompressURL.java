package com.service.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.service.IDecompressURL;

public class DecompressURL implements IDecompressURL {
	final static Logger logger = Logger.getLogger(DecompressURL.class);

	public String findURLByID(Connection connection, int id) throws SQLException {
		logger.info("Attempting to find URL by ID:" + id);
		PreparedStatement selectPreparedStatement = null;
		ResultSet selectRS = null;
		String fullURL = null;

		selectPreparedStatement = connection.prepareStatement("SELECT LONG_URL FROM URL WHERE ID=?");
		selectPreparedStatement.setInt(1, id);
		selectRS = selectPreparedStatement.executeQuery();

		if (selectRS != null && selectRS.next()) {
			fullURL = selectRS.getString("LONG_URL");
			logger.info(" Found URL: " + fullURL + " for ID:" + id);
		}
		return fullURL;
	}
}
