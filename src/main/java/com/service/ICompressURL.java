package com.service;

import java.sql.Connection;
import java.sql.SQLException;

public interface ICompressURL {
	public String addURL(Connection connection, String longURL) throws SQLException;

	public boolean isURLValid(String longURL);
}
