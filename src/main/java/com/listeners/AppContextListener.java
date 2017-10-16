package com.listeners;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.apache.log4j.Logger;
import org.h2.tools.DeleteDbFiles;

import com.service.IDBConnectionManager;
import com.service.impl.DBConnectionManager;

@WebListener
public class AppContextListener implements ServletContextListener {

	final static Logger logger = Logger.getLogger(AppContextListener.class);

	public void contextInitialized(ServletContextEvent servletContextEvent) {
		ServletContext ctx = servletContextEvent.getServletContext();
		String dbURL = ctx.getInitParameter("dbURL");
		String user = ctx.getInitParameter("dbUser");
		String pwd = ctx.getInitParameter("dbPassword");
		IDBConnectionManager connectionManager = null;
		connectionManager = new DBConnectionManager(dbURL, user, pwd);
		ctx.setAttribute("DBConnection", connectionManager.getConnection());
		logger.info("DB Connection initialized successfully");

		try {
			initializeDatabase(connectionManager.getConnection());
		} catch (Exception e) {
			logger.info("DB Connection initialized successfully");
		}
	}

	private void initializeDatabase(Connection connection) throws Exception {
		PreparedStatement createPreparedStatement = null;
		DeleteDbFiles.execute("~", "URL", true);
		String CreateQuery = "CREATE TABLE URL(ID bigint(20) unsigned NOT NULL AUTO_INCREMENT, LONG_URL varchar(255), PRIMARY KEY (`id`))";

		createPreparedStatement = connection.prepareStatement(CreateQuery);
		createPreparedStatement.executeUpdate();
		createPreparedStatement.close();
		logger.info("DB Schema initialized successfully");
	}

	public void contextDestroyed(ServletContextEvent servletContextEvent) {
		Connection con = (Connection) servletContextEvent.getServletContext().getAttribute("DBConnection");
		try {
			con.close();
		} catch (SQLException e) {
			logger.info("SQLException occurred while closing the connection to the DB: " + e.getMessage());
		}
	}

}