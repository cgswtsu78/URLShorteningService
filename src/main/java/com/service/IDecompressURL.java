package com.service;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Interface for validating, decompressing and returning the long URL
 * 
 * @author colingray
 *
 */
public interface IDecompressURL {
	public String findURLByID(Connection connection, int id) throws SQLException;
}
