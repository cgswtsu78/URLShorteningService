package com.service;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Interface for validating, compressing and adding the URL
 * 
 * @author colingray
 *
 */
public interface ICompressURL {
	public String addURL(Connection connection, String longURL) throws SQLException;

	public boolean isURLValid(String longURL);
}
