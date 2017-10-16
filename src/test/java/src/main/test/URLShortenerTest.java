package src.main.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.apache.log4j.Logger;
import org.h2.tools.DeleteDbFiles;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.mockito.Mockito;

import com.service.ICompressURL;
import com.service.IDBConnectionManager;
import com.service.IDecompressURL;
import com.service.impl.CompressURL;
import com.service.impl.DBConnectionManager;
import com.service.impl.DecompressURL;

public class URLShortenerTest extends Mockito {
	final static Logger logger = Logger.getLogger(URLShortenerTest.class);
	private static ICompressURL compressURL = new CompressURL();
	private static IDecompressURL decompressURL = new DecompressURL();
	private static IDBConnectionManager dbConnectionManager = null;
	private static final String DB_DRIVER = "org.h2.Driver";
	private static final String DB_CONNECTION = "jdbc:h2:~/URLTEST";
	private static final String DB_USER = "TESTURLUSER";
	private static final String DB_PASSWORD = "TESTURLUSER123!@";
	private static Connection connection = null;

	@BeforeClass
	public static void initialize() {
		try {
			PreparedStatement createPreparedStatement = null;
			DeleteDbFiles.execute("~", "URL", true);
			String CreateQuery = "CREATE TABLE URLTEST(ID bigint(20) unsigned NOT NULL AUTO_INCREMENT, LONG_URL varchar(255), PRIMARY KEY (`id`))";
			dbConnectionManager = new DBConnectionManager(DB_CONNECTION, DB_USER, DB_PASSWORD);
			connection = dbConnectionManager.getConnection();
			createPreparedStatement = connection.prepareStatement(CreateQuery);
			createPreparedStatement.executeUpdate();
			createPreparedStatement.close();
		} catch (SQLException e) {
			logger.error("SQLException occurred while initializing the DB: " + e.getMessage());
		}
	}

	@org.junit.Test
	public void testURLValidity() {
		assertTrue(compressURL.isURLValid("http://apple.com"));
		assertTrue(compressURL.isURLValid("https://apple.com"));
		assertTrue(compressURL.isURLValid("http://apple.co"));
		assertFalse(compressURL.isURLValid("htts://apple.com"));
		assertFalse(compressURL.isURLValid("htts://apple.com"));
		assertFalse(compressURL.isURLValid("h://apple.com"));
		assertFalse(compressURL.isURLValid("/apple.com"));
		assertFalse(compressURL.isURLValid("http://apple"));

	}

	@org.junit.Test
	public void testValidDBConnection() {
		connection = dbConnectionManager.getConnection();
		assertNotNull(connection);
	}

	@org.junit.Test
	public void testInValidDBConnection() {
		IDBConnectionManager dbConnectionManager2 = null;
		dbConnectionManager2 = new DBConnectionManager("", "", "");
		assertEquals(dbConnectionManager2.getConnection(), null);
	}

	@org.junit.Test
	public void testAddURLValid() {
		try {
			assertTrue(compressURL.addURL(connection, "http://www.apple.com") != null);
		} catch (SQLException e) {
			logger.error("SQLException occurred while initializing the DB: " + e.getMessage());
		}
	}

	@org.junit.Test
	public void testAddURLInvalid() {
		try {
			assertTrue(compressURL.addURL(connection, "htp://www.apple.com") == null);
		} catch (SQLException e) {
			logger.error("SQLException occurred while initializing the DB: " + e.getMessage());
		}
	}

	@org.junit.Test
	public void testAddURLThatExists() {
		try {
			assertEquals(compressURL.addURL(connection, "http://www.apple.com"), "1");
		} catch (SQLException e) {
			logger.error("SQLException occurred while initializing the DB: " + e.getMessage());
		}
	}

	@org.junit.Test
	public void testFindURLByID() {
		try {
			assertEquals(decompressURL.findURLByID(connection, 1), "http://www.apple.com");
		} catch (SQLException e) {
			logger.error("SQLException occurred while initializing the DB: " + e.getMessage());
		}
	}

	@org.junit.Test
	public void testFindIvalidURLByID() {
		try {
			assertEquals(decompressURL.findURLByID(connection, 3), null);
		} catch (SQLException e) {
			logger.error("SQLException occurred while initializing the DB: " + e.getMessage());
		}
	}

	@Ignore
	private static Connection getDBConnection() {
		Connection dbConnection = null;
		try {
			Class.forName(DB_DRIVER);
		} catch (ClassNotFoundException e) {
			logger.error("ClassNotFoundException occurred while initializing the DB: " + e.getMessage());
		}
		try {
			dbConnection = DriverManager.getConnection(DB_CONNECTION, DB_USER, DB_PASSWORD);
			return dbConnection;
		} catch (SQLException e) {
			logger.error("SQLException occurred while initializing the DB: " + e.getMessage());
		}
		return dbConnection;
	}
}
