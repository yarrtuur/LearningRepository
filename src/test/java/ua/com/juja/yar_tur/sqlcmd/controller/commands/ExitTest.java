package ua.com.juja.yar_tur.sqlcmd.controller.commands;

import org.junit.*;
import ua.com.juja.yar_tur.sqlcmd.model.ConnectionKeeper;
import ua.com.juja.yar_tur.sqlcmd.model.DBCommandManager;
import ua.com.juja.yar_tur.sqlcmd.model.JDBCDatabaseManager;
import ua.com.juja.yar_tur.sqlcmd.types_enums_except.CmdLineState;
import ua.com.juja.yar_tur.sqlcmd.types_enums_except.FeedBack;
import ua.com.juja.yar_tur.sqlcmd.viewer.Console;
import ua.com.juja.yar_tur.sqlcmd.viewer.View;

import java.sql.SQLException;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ExitTest {

	private String[] commandLine = new String[]{"exit"};
	private DBCommandManager dbManagerMock = mock(JDBCDatabaseManager.class);
	private ConnectionKeeper connectionKeeperMock = mock(ConnectionKeeper.class);
	private View viewMock = mock(Console.class);
	private Exit commandMock = new Exit(dbManagerMock, viewMock);


	@BeforeClass
	public static void beforeClass() {
		System.out.println("Before ExitTest.class");
	}

	@AfterClass
	public  static void afterClass() {
		System.out.println("After ExitTest.class");
	}


	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void processNoConnectTest() throws Exception {
		when(dbManagerMock.getConnection()).thenReturn(connectionKeeperMock);
		when(dbManagerMock.toExit()).thenThrow(new SQLException());
		assertEquals(CmdLineState.WAIT, commandMock.process(commandLine));
	}

	@Test
	public void processTest() throws Exception {
		when(dbManagerMock.toExit()).thenReturn(FeedBack.OK);
		assertEquals(CmdLineState.EXIT, commandMock.process(commandLine));
	}

	@Test
	public void canProcessTest() {
		String singleCommand = "exit";
		assertEquals(true, commandMock.canProcess(singleCommand));
	}

	@After
	public void tearDown() throws Exception {
	}

}