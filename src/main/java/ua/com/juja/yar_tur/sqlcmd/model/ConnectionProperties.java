package ua.com.juja.yar_tur.sqlcmd.model;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class ConnectionProperties {

	private Properties connProperties = new Properties();

	public ConnectionProperties() {
		try (
				InputStream in = getClass().getResourceAsStream("../resourses/conn.properties");
		) {
			connProperties.load(in);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String getConnDbName() {
		return connProperties.getProperty("POSTGRE_DB_NAME");
	}

	public String getConnDbPasswd() {
		return connProperties.getProperty("POSTGRE_DB_PASSWD");
	}

	public String getConnDbLogin() {
		return connProperties.getProperty("POSTGRE_DB_LOGIN");
	}

	public String getConnDbSocket() {
		return connProperties.getProperty("POSTGRE_DB_SOCKET");
	}


}
