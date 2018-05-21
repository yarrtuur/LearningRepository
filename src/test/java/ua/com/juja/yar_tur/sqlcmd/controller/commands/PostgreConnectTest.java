package ua.com.juja.yar_tur.sqlcmd.controller.commands;

import org.junit.*;
import ua.com.juja.yar_tur.sqlcmd.model.CommandProcess;
import ua.com.juja.yar_tur.sqlcmd.model.ConnectionKeeper;
import ua.com.juja.yar_tur.sqlcmd.model.DBCommandManager;
import ua.com.juja.yar_tur.sqlcmd.model.JDBCDatabaseManager;
import ua.com.juja.yar_tur.sqlcmd.types_enums_except.CmdLineState;
import ua.com.juja.yar_tur.sqlcmd.types_enums_except.FeedBack;
import ua.com.juja.yar_tur.sqlcmd.viewer.Console;
import ua.com.juja.yar_tur.sqlcmd.viewer.View;

import java.sql.SQLException;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PostgreConnectTest {
	private String singleCommand;
	private String[] commandLine;
	private DBCommandManager dbManagerMock = mock(JDBCDatabaseManager.class);
	private ConnectionKeeper connectionKeeperMock = mock(ConnectionKeeper.class);
	private View viewMock = mock(Console.class);
	private CommandProcess command;

	@BeforeClass
	public static void beforeClass() {
		System.out.println("Before PostgreConnectTest.class");
	}

	@AfterClass
	public  static void afterClass() {
		System.out.println("After PostgreConnectTest.class");
	}


	@Before
	public void setUp() throws Exception {
		command = new PostgreConnect(dbManagerMock, viewMock);
		commandLine = new String[]{"connect"};
		singleCommand = "connect";
	}

	@After
	public void tearDown() throws Exception {
		command = null;
		commandLine = null;
		singleCommand = null;
	}

	@Test
	public void canProcess() throws SQLException {
		when(dbManagerMock.toConnect(anyString(),anyString(),anyString())).thenReturn(FeedBack.OK);
		assertEquals(true, command.canProcess(singleCommand));
	}

	@Test
	public void processNoConnectTest() throws SQLException {
		when(dbManagerMock.getConnection()).thenReturn(connectionKeeperMock);
		when(dbManagerMock.toConnect(anyString(),anyString(),anyString())).thenThrow(new SQLException());
		assertEquals(CmdLineState.WAIT, command.process(commandLine));
	}

	@Test
	public void process() throws SQLException {
		when(dbManagerMock.toConnect(anyString(),anyString(),anyString())).thenReturn(FeedBack.OK);
		assertEquals(CmdLineState.WAIT, command.process(commandLine));
	}

/*	@Test
	public void setSocketProperties() throws SQLException {
	}

	@Test
	public void setSocketData() {
	}*/
}