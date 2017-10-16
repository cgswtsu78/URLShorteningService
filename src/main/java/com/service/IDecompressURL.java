package com.service;

import java.sql.Connection;
import java.sql.SQLException;

public interface IDecompressURL {
	public String findURLByID(Connection connection, int id) throws SQLException;
}
