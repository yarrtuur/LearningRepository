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
	public void testChkConnectionOn() {
		// given
		in.add("connect");
		in.add("chkconn");
        in.add("exit");
		// when
		Main.main(new String[1]);
		// then
		Assert.assertEquals("Hello, user!" + CARET +
						"Please, type `help` for list available commands. " + CARET +
						"-------- PostgreSQL JDBC Connection Testing ------------" + CARET +
						"PostgreSQL JDBC Driver Registered!" + CARET +
						"You made it, take control your database now!" + CARET +
                        "Connection has already done" + CARET +
                        "Connection closed." + CARET
				, getData());
	}

	@Test
	public void testChkConnectionOff() {
		// given
		in.add("chkconn");
        in.add("exit");
		// when
		Main.main(new String[1]);
		// then
		Assert.assertEquals("Hello, user!" + CARET +
						"Please, type `help` for list available commands. " + CARET +
						"Connection hasn`t done yet" + CARET +
                        "Connection closed." + CARET
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
        in.add("exit");
		// when
		Main.main(new String[1]);
		// then
		Assert.assertEquals("Hello, user!" + CARET +
						"Please, type `help` for list available commands. " + CARET +
						"-------- PostgreSQL JDBC Connection Testing ------------" + CARET +
						"PostgreSQL JDBC Driver Registered!" + CARET +
						"You made it, take control your database now!" + CARET +
						"Create table vaza successfull." + CARET +
						"Insert data into table successfull for 1 rows" + CARET +
						"Delete data operation successfull for 1 rows" + CARET +
						"Drop table vaza successfull" + CARET +
                        "Connection closed." + CARET
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
        in.add("exit");
		// when
		Main.main(new String[1]);
		// then
		Assert.assertEquals("Hello, user!" + CARET +
						"Please, type `help` for list available commands. " + CARET +
						"-------- PostgreSQL JDBC Connection Testing ------------" + CARET +
						"PostgreSQL JDBC Driver Registered!" + CARET +
						"You made it, take control your database now!" + CARET +
						"Create table vaza successfull." + CARET +
						"Insert data into table successfull for 1 rows" + CARET +
						" | rid | flower | " + CARET +
						" | 1 | rose       | " + CARET +
                        "Find data successfull" + CARET +
						"Drop table vaza successfull" + CARET +
                        "Connection closed." + CARET
				, getData());
	}

	@Test
	public void testDataInserter() {
		// given
		in.add("connect");
		in.add("create|vaza|flower|char(10)");
		in.add("insert|vaza|flower|rose");
		in.add("drop|vaza");
        in.add("exit");
		// when
		Main.main(new String[1]);
		// then
		Assert.assertEquals("Hello, user!" + CARET +
						"Please, type `help` for list available commands. " + CARET +
						"-------- PostgreSQL JDBC Connection Testing ------------" + CARET +
						"PostgreSQL JDBC Driver Registered!" + CARET +
						"You made it, take control your database now!" + CARET +
						"Create table vaza successfull." + CARET +
						"Insert data into table successfull for 1 rows" + CARET +
						"Drop table vaza successfull" + CARET +
                        "Connection closed." + CARET
				, getData());
	}

	@Test
	public void testDataUpdater() {
		// given
		in.add("connect");
		in.add("create|vaza|flower|char(10)");
		in.add("insert|vaza|flower|rose");
		in.add("update|vaza|set|flower|pion|where|flower|rose");
		in.add("drop|vaza");
        in.add("exit");
		// when
		Main.main(new String[1]);
		// then
		Assert.assertEquals("Hello, user!" + CARET +
						"Please, type `help` for list available commands. " + CARET +
						"-------- PostgreSQL JDBC Connection Testing ------------" + CARET +
						"PostgreSQL JDBC Driver Registered!" + CARET +
						"You made it, take control your database now!" + CARET +
						"Create table vaza successfull." + CARET +
						"Insert data into table successfull for 1 rows" + CARET +
						"Update data successfull" + CARET +
						"Drop table vaza successfull" + CARET +
                        "Connection closed." + CARET
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
						"Connection closed." + CARET
				, getData());
	}

	@Test
	public void testHelp() {
		// given
		in.add("help");
        in.add("exit");
		// when
        Main.main(new String[1]);
		// then
		Assert.assertEquals("Hello, user!" + CARET +
						"Please, type `help` for list available commands. " + CARET +
						"List of commands:" + CARET +
						" exit : exit" + CARET +
						" help : help" + CARET +
						" connect : connect " + CARET +
						" create : create | tableName | column1 | type1 | ... | columnN " + CARET +
						" tables : tables OR tables | tableName " + CARET +
						" insert : insert | tableName | column1 | value1 | column2 | value2 | ... | columnN | valueN " + CARET +
						" find : find | tableName OR  find | tableName | column | value " + CARET +
						" clear : clear | tableName " + CARET +
						" drop : drop | tableName " + CARET +
						" delete : delete | tableName | column | value " + CARET +
						" update : update | tableName | SET | column1 | value1 | column2 | value2 | WHERE| columnX | valueX " + CARET +
                        " chkconn : chkconn " + CARET +
                        "Connection closed." + CARET
				, getData());
	}

	@Test
    public void testPostgreConnectOn() {
        // given
        in.add("connect");
        in.add("exit");
        // when
        Main.main(new String[1]);
        // then
        Assert.assertEquals("Hello, user!" + CARET +
                        "Please, type `help` for list available commands. " + CARET +
						"-------- PostgreSQL JDBC Connection Testing ------------" + CARET +
						"PostgreSQL JDBC Driver Registered!" + CARET +
                        "You made it, take control your database now!" + CARET +
                        "Connection closed." + CARET
                , getData());
    }

	@Test
	public void testTableCleaner() {
		// given
		in.add("connect");
		in.add("create|vaza|flower|char(10)");
		in.add("insert|vaza|flower|rose");
		in.add("find|vaza");
		in.add("clear|vaza");
		in.add("find|vaza");
		in.add("drop|vaza");
        in.add("exit");

		// when
		Main.main(new String[1]);
		// then
		Assert.assertEquals("Hello, user!" + CARET +
						"Please, type `help` for list available commands. " + CARET +
						"-------- PostgreSQL JDBC Connection Testing ------------" + CARET +
						"PostgreSQL JDBC Driver Registered!" + CARET +
						"You made it, take control your database now!" + CARET +
						"Create table vaza successfull." + CARET +
						"Insert data into table successfull for 1 rows" + CARET +
						" | rid | flower | " + CARET +
 						" | 1 | rose       | " + CARET +
                        "Find data successfull" + CARET +
						"Clear table successfull for 1 rows" + CARET +
						" | rid | flower | " + CARET +
                        "Find data successfull" + CARET +
						"Drop table vaza successfull" + CARET +
                        "Connection closed." + CARET
				, getData());
	}

	@Test
	public void testTableCreater() {
		// given
		in.add("connect");
		in.add("create|vaza|flower|char");
		in.add("drop|vaza");
        in.add("exit");
		// when
		Main.main(new String[1]);
		// then
		Assert.assertEquals("Hello, user!" + CARET +
						"Please, type `help` for list available commands. " + CARET +
						"-------- PostgreSQL JDBC Connection Testing ------------" + CARET +
						"PostgreSQL JDBC Driver Registered!" + CARET +
						"You made it, take control your database now!" + CARET +
						"Create table vaza successfull." + CARET +
						"Drop table vaza successfull" + CARET +
                        "Connection closed." + CARET
				, getData());
	}

	@Test
	public void testTableDroper() {
		// given
		in.add("connect");
		in.add("create|vaza|flower|char(10)");
		in.add("drop|vaza");
        in.add("exit");

		// when
		Main.main(new String[1]);
		// then
		Assert.assertEquals("Hello, user!" + CARET +
						"Please, type `help` for list available commands. " + CARET +
						"-------- PostgreSQL JDBC Connection Testing ------------" + CARET +
						"PostgreSQL JDBC Driver Registered!" + CARET +
						"You made it, take control your database now!" + CARET +
						"Create table vaza successfull." + CARET +
						"Drop table vaza successfull" + CARET +
                        "Connection closed." + CARET
				, getData());
	}

	@Test
	public void testTableViewer() {
		// given
		in.add("connect");
		in.add("create|vaza|flower|char(10)");
		in.add("tables|vaza");
		in.add("drop|vaza");
        in.add("exit");
		// when
		Main.main(new String[1]);
		// then
		Assert.assertEquals("Hello, user!" + CARET +
						"Please, type `help` for list available commands. " + CARET +
						"-------- PostgreSQL JDBC Connection Testing ------------" + CARET +
						"PostgreSQL JDBC Driver Registered!" + CARET +
						"You made it, take control your database now!" + CARET +
						"Create table vaza successfull." + CARET +
						"Table: vaza , Columns:  rid:integer, flower:character " + CARET +
						"Drop table vaza successfull" + CARET +
                        "Connection closed." + CARET
				, getData());
	}

    @Test
	public void testAllTableViewer() {
        // given
        in.add("connect");
        in.add("create|vaza|flower|char(10)");
        in.add("create|userz|name|char(10)");
        in.add("create|tbl|form|char(10)");
        in.add("tables");
        in.add("drop|vaza");
        in.add("drop|userz");
        in.add("drop|tbl");
        in.add("exit");
        // when
        Main.main(new String[1]);
        // then
        Assert.assertEquals("Hello, user!" + CARET +
                        "Please, type `help` for list available commands. " + CARET +
                        "-------- PostgreSQL JDBC Connection Testing ------------" + CARET +
                        "PostgreSQL JDBC Driver Registered!" + CARET +
                        "You made it, take control your database now!" + CARET +
						"Create table vaza successfull." + CARET +
						"Create table userz successfull." + CARET +
						"Create table tbl successfull." + CARET +
                        "Tables:  tbl, userz, vaza  " + CARET +
						"Drop table vaza successfull" + CARET +
						"Drop table userz successfull" + CARET +
						"Drop table tbl successfull" + CARET +
                        "Connection closed." + CARET
                , getData());
    }

	@Test
	public void testAllTableViewerifNoTables() {//todo
		// given
		in.add("connect");
		in.add("tables");
		in.add("exit");
		// when
		Main.main(new String[1]);
		// then
		Assert.assertEquals("Hello, user!" + CARET +
						"Please, type `help` for list available commands. " + CARET +
						"-------- PostgreSQL JDBC Connection Testing ------------" + CARET +
						"PostgreSQL JDBC Driver Registered!" + CARET +
						"You made it, take control your database now!" + CARET +
						"There are no tables in the database" + CARET +
						"Connection closed." + CARET
				, getData());
	}

	@Test
	public void testUnreachable() {
		// given
		in.add("single");
        in.add("exit");
		// when
		Main.main(new String[1]);
		// then
		Assert.assertEquals("Hello, user!" + CARET +
						"Please, type `help` for list available commands. " + CARET +
						"Not available command. Maybe your connection hasn`t done yet. " +
                        "Please type `help` to list all commands " + CARET +
                        "Connection closed." + CARET
				, getData());
	}


	private String getData() {
		try {
			return new String(out.toByteArray(), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			return e.getMessage();
		}
	}
}
