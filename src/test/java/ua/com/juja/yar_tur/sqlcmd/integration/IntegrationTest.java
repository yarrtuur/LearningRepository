package ua.com.juja.yar_tur.sqlcmd.integration;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ua.com.juja.yar_tur.sqlcmd.controller.MainController;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;

public class IntegrationTest {
	private static ConfigurableInputStream in;
	private static ByteArrayOutputStream out;
	private final String CARET = System.getProperty("line.separator");
	private MainController dialogue;

	@Before
	public void setup() {
		out = new ByteArrayOutputStream();
		in = new ConfigurableInputStream();
		System.setIn(in);
		System.setOut(new PrintStream(out));
		dialogue = new MainController();
	}



	@Test
	public void testHelp() {
		// given
		in.add("help");
		// when
		//MainController dialogue = new MainController();
		dialogue.mainDialogueHolder();
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
		//MainController dialogue = new MainController();
		dialogue.mainDialogueHolder();
		// then
		Assert.assertEquals("Hello, user!" + CARET +
				"Please, type `help` for list available commands. " + CARET +
				"Closing connection..." + CARET +
				"Connection closed." + CARET
				, getData());
	}

	@Test
	public void testChkConnectionOn() {

		in.add("connect");
		dialogue.mainDialogueHolder();
		in.add("chkconn");
		dialogue.mainDialogueHolder();

		// then
		Assert.assertEquals("Hello, user!" + CARET +
						"Please, type `help` for list available commands. " + CARET +
						"Starting connect..." + CARET +
						"-------- PostgreSQL JDBC Connection Testing ------------" + CARET +
						"PostgreSQL JDBC Driver Registered!" + CARET +
						"You made it, take control your database now!" + CARET +
						"Hello, user!" + CARET +
						"Please, type `help` for list available commands. " + CARET +
						"Not available command. Maybe your connection hasn`t done yet. Please type `help` to list all commands " + CARET +
						"Connection has already done" + CARET
				, getData());
	}

	@Test
	public void testChkConnectionOff() {
		// given
		in.add("chkconn");
		// when
		dialogue.mainDialogueHolder();
		// then
		Assert.assertEquals("Hello, user!" + CARET +
						"Please, type `help` for list available commands. " + CARET +
						"Connection hasnn`t done yet" + CARET
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
