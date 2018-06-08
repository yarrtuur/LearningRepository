package ua.com.juja.yar_tur.sqlcmd.integration;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ua.com.juja.yar_tur.sqlcmd.Main;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;

public class IntegrationTest {
	private static ConfigurableInputStream in;
	private static ByteArrayOutputStream out;
	private final String CARET = System.getProperty("line.separator");

	@Before
	public void setup() {
		out = new ByteArrayOutputStream();
		in = new ConfigurableInputStream();
		System.setIn(in);
		System.setOut(new PrintStream(out));
	}

	@Test
	public void testHelp() {
		// given
		in.add("help");
		// when
        Main.main(new String[1]);
		// then
		Assert.assertEquals("Hello, user!" + CARET +
						"Please, type `help` for list available commands. " + CARET +
						"List of commands:" + CARET +
						" exit : exit" + CARET +
						" help : help" + CARET +
						" connect : connect OR connect | username | password | database_sid | <IP-addr> | <port> " + CARET +
						" create : create | tableName | column1 | type1 | ... | columnN " + CARET +
						" tables : tables OR tables | tableName " + CARET +
						" insert : insert | tableName | column1 | value1 | column2 | value2 | ... | columnN | valueN " + CARET +
						" find : find | tableName OR  find | tableName | column | value " + CARET +
						" clear : clear | tableName " + CARET +
						" drop : drop | tableName " + CARET +
						" delete : delete | tableName | column | value " + CARET +
						" update : update | tableName | SET | column1 | value1 | column2 | value2 | WHERE| columnX | valueX " + CARET +
						" chkconn : chkconn " + CARET
				, getData());
	}

	@Test
	public void testExit() {
		// given
		in.add("exit");
		// when
        Main.main(new String[1]);
		// then
		Assert.assertEquals("Hello, user!" + CARET +
				"Please, type `help` for list available commands. " + CARET +
				"Closing connection..." + CARET +
				"Connection closed." + CARET
				, getData());
	}

	@Test
	public void testChkConnectionOn() {
        // given
        in.add("connect");
        in.add("chkconn");
        // when
        Main.main(new String[1]);
        // then
		Assert.assertEquals("Hello, user!" + CARET +
						"Please, type `help` for list available commands. " + CARET +
						"Starting connect..." + CARET +
						"-------- PostgreSQL JDBC Connection Testing ------------" + CARET +
						"PostgreSQL JDBC Driver Registered!" + CARET +
						"You made it, take control your database now!" + CARET +
						"Connection has already done" + CARET
				, getData());
	}

	@Test
	public void testChkConnectionOff() {
		// given
		in.add("chkconn");
		// when
        Main.main(new String[1]);
		// then
		Assert.assertEquals("Hello, user!" + CARET +
						"Please, type `help` for list available commands. " + CARET +
						"Connection hasnn`t done yet" + CARET
				, getData());
	}

	@Test
    public void testPostgreConnectOn() {
        // given
        in.add("connect");
        // when
        Main.main(new String[1]);
        // then
        Assert.assertEquals("Hello, user!" + CARET +
                        "Please, type `help` for list available commands. " + CARET +
                        "Starting connect..." + CARET +
                        "-------- PostgreSQL JDBC Connection Testing ------------" + CARET +
                        "PostgreSQL JDBC Driver Registered!" + CARET +
                        "You made it, take control your database now!" + CARET
                , getData());
    }

    @Test
    public void testTableCreater() {
        // given
        in.add("connect");
        in.add("create|vaza|flower|char");
		in.add("drop|vaza");
		// when
        Main.main(new String[1]);
        // then
        Assert.assertEquals("Hello, user!" + CARET +
                        "Please, type `help` for list available commands. " + CARET +
                        "Starting connect..." + CARET +
                        "-------- PostgreSQL JDBC Connection Testing ------------" + CARET +
                        "PostgreSQL JDBC Driver Registered!" + CARET +
                        "You made it, take control your database now!" + CARET +
						"Creating table..." + CARET +
						"Create table successfull." + CARET +
						"Droping table..." + CARET +
						"Drop table successfull" + CARET
				, getData());
    }

	@Test
	public void testTableDroper() {
		// given
		in.add("connect");
		in.add("create|vaza|flower|char(10)");
		in.add("drop|vaza");
		// when
		Main.main(new String[1]);
		// then
		Assert.assertEquals("Hello, user!" + CARET +
						"Please, type `help` for list available commands. " + CARET +
						"Starting connect..." + CARET +
						"-------- PostgreSQL JDBC Connection Testing ------------" + CARET +
						"PostgreSQL JDBC Driver Registered!" + CARET +
						"You made it, take control your database now!" + CARET +
						"Creating table..." + CARET +
						"Create table successfull." + CARET +
						"Droping table..." + CARET +
						"Drop table successfull" + CARET
				, getData());
	}

	@Test
	public void testDataInserter() {
		// given
		in.add("connect");
		in.add("create|vaza|flower|char(10)");
		in.add("insert|vaza|flower|rose");
		in.add("drop|vaza");
		// when
		Main.main(new String[1]);
		// then
		Assert.assertEquals("Hello, user!" + CARET +
						"Please, type `help` for list available commands. " + CARET +
						"Starting connect..." + CARET +
						"-------- PostgreSQL JDBC Connection Testing ------------" + CARET +
						"PostgreSQL JDBC Driver Registered!" + CARET +
						"You made it, take control your database now!" + CARET +
						"Creating table..." + CARET +
						"Create table successfull." + CARET +
						"Inserting data." + CARET +
						"Insert data into table successfull" + CARET +
						"Droping table..." + CARET +
						"Drop table successfull" + CARET
				, getData());
	}

	@Test
	public void testDataFinder() {
		// given
		in.add("connect");
		in.add("create|vaza|flower|char(10)");
		in.add("insert|vaza|flower|rose");
		in.add("find|vaza");
		in.add("drop|vaza");
		// when
		Main.main(new String[1]);
		// then
		Assert.assertEquals("Hello, user!" + CARET +
						"Please, type `help` for list available commands. " + CARET +
						"Starting connect..." + CARET +
						"-------- PostgreSQL JDBC Connection Testing ------------" + CARET +
						"PostgreSQL JDBC Driver Registered!" + CARET +
						"You made it, take control your database now!" + CARET +
						"Creating table..." + CARET +
						"Create table successfull." + CARET +
						"Inserting data." + CARET +
						"Insert data into table successfull" + CARET +
						"Finding table..." + CARET +
 						" | rid | flower | " + CARET +
 						" | 1 | rose       | " + CARET +
						"Find data successfull." + CARET +
						"Droping table..." + CARET +
						"Drop table successfull" + CARET
				, getData());
	}

	@Test
	public void testDataDeleter() {
		// given
		in.add("connect");
		in.add("create|vaza|flower|char(10)");
		in.add("insert|vaza|flower|rose");
		in.add("delete|vaza|flower|rose");
		in.add("drop|vaza");
		// when
		Main.main(new String[1]);
		// then
		Assert.assertEquals("Hello, user!" + CARET +
						"Please, type `help` for list available commands. " + CARET +
						"Starting connect..." + CARET +
						"-------- PostgreSQL JDBC Connection Testing ------------" + CARET +
						"PostgreSQL JDBC Driver Registered!" + CARET +
						"You made it, take control your database now!" + CARET +
						"Creating table..." + CARET +
						"Create table successfull." + CARET +
						"Inserting data." + CARET +
						"Insert data into table successfull" + CARET +
						"Finding table..." + CARET +
						" | rid | flower | " + CARET +
						" | 1 | rose       | " + CARET +
						"Find data successfull." + CARET +
						"Droping table..." + CARET +
						"Drop table successfull" + CARET
				, getData());
	}


	public String getData() {
		try {
			return new String(out.toByteArray(), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			return e.getMessage();
		}
	}
}
