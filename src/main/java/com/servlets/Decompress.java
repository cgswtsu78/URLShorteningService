package com.servlets;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.service.impl.DecompressURL;

@WebServlet("/u/*")
public class Decompress extends HttpServlet {
	private static final long serialVersionUID = 1L;
	final static Logger logger = Logger.getLogger(Decompress.class);
	DecompressURL decompressURL = new DecompressURL();

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Connection connection = (Connection) getServletContext().getAttribute("DBConnection");
		String longURL = null;
		String requestURL = request.getRequestURI();
		logger.info("request URL:" + requestURL);
		String[] pathParts = requestURL.split("/");
		if (requestURL.contains("index.html") || pathParts.length != 3) {
			sendInvalidRedirect(response);
		}
		int tinyId = 0;
		try {
			tinyId = Integer.parseInt(pathParts[pathParts.length - 1]);
		} catch (Exception e) {
			logger.error("Invalid data: " + tinyId + " redirecting to invalid.html" + e.getMessage());
			sendInvalidRedirect(response);
		}

		try {
			longURL = decompressURL.findURLByID(connection, tinyId);
		} catch (SQLException e) {
			logger.error("SQLException occurred for ID: " + tinyId + " redirecting to invalid.html" + e.getMessage());
			sendInvalidRedirect(response);
		}
		if (longURL == null || longURL.isEmpty()) {
			sendInvalidRedirect(response);
		}
		response.sendRedirect(longURL);
	}

	private void sendInvalidRedirect(HttpServletResponse response) throws IOException {
		response.sendRedirect("/invalid.html");
	}

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
}
