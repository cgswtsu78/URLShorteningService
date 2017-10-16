package com.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.service.IDBConnectionManager;
import com.service.impl.CompressURL;

/**
 * Servlet which receives the user request with the long URL to shorten
 * 
 * @author colingray
 *
 */
@WebServlet("/compress")
public class Compress extends HttpServlet {
	private static final long serialVersionUID = 1L;
	final static Logger logger = Logger.getLogger(Compress.class);
	private final static String HOST = "http://localhost:8080/u/";
	IDBConnectionManager dbConnectionManager = null;
	CompressURL compressURL = new CompressURL();

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Connection connection = (Connection) getServletContext().getAttribute("DBConnection");
		String longURL = request.getParameter("longURL");
		logger.info("User Entered Long URL:" + longURL);
		if (longURL == null || longURL.isEmpty() || !compressURL.isURLValid(longURL)) {
			respond(request, response, "Invalid URL");
		} else {
			String shortId = null;
			try {
				shortId = compressURL.addURL(connection, longURL);
			} catch (SQLException e) {
				logger.error(
						"SQLException occurred for ID: " + shortId + " redirecting to invalid.html" + e.getMessage());
				sendInvalidRedirect(response);
			}
			respond(request, response, HOST + shortId);
		}

	}

	private void respond(HttpServletRequest request, HttpServletResponse response, String data)
			throws IOException, ServletException {
		RequestDispatcher rd = getServletContext().getRequestDispatcher("/index.html");
		PrintWriter out = response.getWriter();
		out.println("<font color=red>" + data + "</font></br>");
		rd.include(request, response);
	}

	private void sendInvalidRedirect(HttpServletResponse response) throws IOException {
		response.sendRedirect("/invalid.html");
	}

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
}
