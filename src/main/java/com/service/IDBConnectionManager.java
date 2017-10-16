package com.service;

import java.sql.Connection;

public interface IDBConnectionManager {
	public Connection getConnection();
}
