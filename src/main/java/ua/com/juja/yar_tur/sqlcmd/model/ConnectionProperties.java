package ua.com.juja.yar_tur.sqlcmd.model;

import ua.com.juja.yar_tur.sqlcmd.types_enums_except.ExitException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class ConnectionProperties {

	private Properties connProperties = new Properties();
	private static final String CONFIG_SQLCMD_PROPERTIES = "config/sqlcmd.properties";
	private ClassLoader classloader = Thread.currentThread().getContextClassLoader();

	public ConnectionProperties() throws ExitException {
			try (
					InputStream in = classloader.getResourceAsStream(CONFIG_SQLCMD_PROPERTIES);
			) {
				connProperties.load(in);
			} catch (IOException | NullPointerException e) {
				throw new ExitException("No file .properties found in " + e.getMessage());
			}
	}

	public String getConnDbName() {
		return connProperties.getProperty("DB_NAME");
	}

	public String getConnDbPasswd() {
		return connProperties.getProperty("DB_PASSWD");
	}

	public String getConnDbLogin() {
		return connProperties.getProperty("DB_LOGIN");
	}

	public String getConnDbSocket() {
		return connProperties.getProperty("DB_SOCKET");
	}


}
