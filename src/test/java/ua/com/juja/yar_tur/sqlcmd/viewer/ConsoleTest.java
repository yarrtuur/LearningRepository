package ua.com.juja.yar_tur.sqlcmd.viewer;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.Assert.assertEquals;

public class ConsoleTest {
	private static ByteArrayOutputStream out;
	private  static  ByteArrayInputStream in;
	private View view;
	private final String CARET = System.getProperty("line.separator");

	@Before
	public void setUp() throws Exception {
		out = new ByteArrayOutputStream();
		System.setOut(new PrintStream(out));
		view = new Console();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void write() {
		String message = "Hello!" ;
		view.write(message);
		String actual = out.toString();
		assertEquals(message+ CARET, actual);
	}

	@Test
	public void writeBlanc() {
		String message = "" ;
		view.write(message);
		String actual = out.toString();
		assertEquals(message+ CARET, actual);
	}
	@Test
	public void read() {
		String message = "help";
		in = new ByteArrayInputStream(message.getBytes());
		System.setIn(in);
		assertEquals(message, view.read());

	}

	@Test
	public void readBlank() {
		String message = "";
		in = new ByteArrayInputStream(message.getBytes());
		System.setIn(in);
		assertEquals(message + CARET, view.read());

	}
}