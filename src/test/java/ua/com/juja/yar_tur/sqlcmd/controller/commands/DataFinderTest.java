package ua.com.juja.yar_tur.sqlcmd.controller.commands;

import org.junit.*;
import ua.com.juja.yar_tur.sqlcmd.model.ConnectionKeeper;
import ua.com.juja.yar_tur.sqlcmd.model.DBCommandManager;
import ua.com.juja.yar_tur.sqlcmd.model.JDBCDatabaseManager;
import ua.com.juja.yar_tur.sqlcmd.utils.CmdLineState;
import ua.com.juja.yar_tur.sqlcmd.utils.DataContainer;
import ua.com.juja.yar_tur.sqlcmd.viewer.Console;
import ua.com.juja.yar_tur.sqlcmd.viewer.View;

import java.sql.SQLException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class DataFinderTest {
	private String singleCommand;
	private String[] commandLine;
	private DBCommandManager dbManagerMock = mock(JDBCDatabaseManager.class);
	private ConnectionKeeper connectionKeeperMock = mock(ConnectionKeeper.class);
	private View viewMock = mock(Console.class);
	private DataFinder command;

	@BeforeClass
	public static void setUpClass() {
		System.out.println("Before DataFinderTest.class");
	}

	@AfterClass
	public static void tearDownClass() {
		System.out.println("After DataFinderTest.class");
	}

	@Before
	public void setUp() {
		singleCommand = "find";
		command = new DataFinder(dbManagerMock, viewMock);
	}

	@After
	public void tearDown() {
		singleCommand = null;
		commandLine = null;
		command = null;
	}

	@Test
	public void canProcess() {
		when(dbManagerMock.getConnection()).thenReturn(connectionKeeperMock);
		when(dbManagerMock.getConnection().isConnected()).thenReturn(true);
		assertTrue(command.canProcess(singleCommand));
	}

	@Test
	public void process() throws SQLException {
		commandLine = new String[]{"find","tbl"};
		when(dbManagerMock.getConnection()).thenReturn(connectionKeeperMock);
		when(dbManagerMock.getConnection().isConnected()).thenReturn(true);
		doNothing().when(dbManagerMock).toFind(any(DataContainer.class));
		assertEquals(CmdLineState.WAIT, command.process(commandLine));
	}

	@Test
	public void processWithSQLE() throws SQLException {
		commandLine = new String[]{"find","tbl"};
		when(dbManagerMock.getConnection()).thenReturn(connectionKeeperMock);
		when(dbManagerMock.getConnection().isConnected()).thenReturn(true);
		doThrow(SQLException.class).when(dbManagerMock).toFind(any(DataContainer.class));
		assertEquals(CmdLineState.WAIT, command.process(commandLine));
	}

	@Test
	public void processWithNoTablename() throws SQLException {
		commandLine = new String[]{"find"};
		when(dbManagerMock.getConnection()).thenReturn(connectionKeeperMock);
		when(dbManagerMock.getConnection().isConnected()).thenReturn(true);
		doNothing().when(dbManagerMock).toFind(any(DataContainer.class));
		assertEquals(CmdLineState.WAIT, command.process(commandLine));
	}

	@Test
	public void processWithWrongCountArgs() throws SQLException {
		commandLine = new String[]{"find","tbl","fld"};
		when(dbManagerMock.getConnection()).thenReturn(connectionKeeperMock);
		when(dbManagerMock.getConnection().isConnected()).thenReturn(true);
		doNothing().when(dbManagerMock).toFind(any(DataContainer.class));
		assertEquals(CmdLineState.WAIT, command.process(commandLine));
	}

	@Test
	public void processWithDetails() throws SQLException {
		commandLine = new String[]{"find","tbl","fld","dat"};
		when(dbManagerMock.getConnection()).thenReturn(connectionKeeperMock);
		when(dbManagerMock.getConnection().isConnected()).thenReturn(true);
		doNothing().when(dbManagerMock).toFind(any(DataContainer.class));
		assertEquals(CmdLineState.WAIT, command.process(commandLine));
	}

}