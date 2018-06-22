package ua.com.juja.yar_tur.sqlcmd.viewer;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;

import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ConsoleMockTest {
	private static ByteArrayInputStream inMock = mock(ByteArrayInputStream.class);
	private BufferedReader readerMock = mock(BufferedReader.class);
	private View view;

	@Before
	public void setUp() throws Exception {
		view = new Console();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void readBlank() throws IOException {
		System.setIn(inMock);
		when(readerMock.readLine()).thenThrow(new IOException());
		assertNull(view.read());
	}
}