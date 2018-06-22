package ua.com.juja.yar_tur.sqlcmd.viewer;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class ConsoleTest {
	private InputStream stdin;
	private BufferedReader reader;
	private View view;
	private final String CARET = System.getProperty("line.separator");
	private String data;
	private ByteArrayOutputStream stdOut;



	@Before
	public void setUp() {
		stdin = System.in;
		stdOut = new ByteArrayOutputStream();
		System.setOut(new PrintStream(stdOut));
		view = new Console();

	}

	@After
	public void tearDown() {
		System.setIn(stdin);
	}

	@Test
	public void write() {
		String data = "Hello!";
		view.write(data);
		String actual = stdOut.toString();
		assertEquals(data + CARET, actual);
	}

	@Test
	public void read()  {
		data = "help";
		System.setIn(new ByteArrayInputStream(data.getBytes()));
		reader = new BufferedReader(new InputStreamReader(System.in));
		assertEquals(data, view.read());

	}

	@Test
	public void readBlank()   {
		data = "";
		System.setIn(new ByteArrayInputStream(data.getBytes()));
		reader = new BufferedReader(new InputStreamReader(System.in));
		assertNull(data, view.read());
	}
}