package ua.com.juja.yar_tur.sqlcmd.controller.commands;

import org.junit.*;
import ua.com.juja.yar_tur.sqlcmd.model.ConnectionKeeper;
import ua.com.juja.yar_tur.sqlcmd.model.DBCommandManager;
import ua.com.juja.yar_tur.sqlcmd.model.JDBCDatabaseManager;
import ua.com.juja.yar_tur.sqlcmd.utils.CmdLineState;
import ua.com.juja.yar_tur.sqlcmd.utils.DataContainer;
import ua.com.juja.yar_tur.sqlcmd.utils.ExitException;
import ua.com.juja.yar_tur.sqlcmd.viewer.Console;
import ua.com.juja.yar_tur.sqlcmd.viewer.View;

import java.sql.SQLException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

public class TableCreateTest {
	private String singleCommand;
	private String[] commandLine;
	private DBCommandManager dbManagerMock = mock(JDBCDatabaseManager.class);
	private ConnectionKeeper connectionKeeperMock = mock(ConnectionKeeper.class);
	private View viewMock = mock(Console.class);
	private TableCreate command;

	@BeforeClass
	public static void setUpClass() {
		System.out.println("Before TableCreateTest.class");
	}

	@AfterClass
	public static void tearDownClass() {
		System.out.println("After TableCreateTest.class");
	}

	@Before
	public void setUp() {
		singleCommand = "create";
		command = new TableCreate(dbManagerMock, viewMock);
	}

	@After
	public void tearDown() {
		singleCommand = null;
		command = null;
		commandLine = null;
	}

	@Test
	public void canProcess() {
		when(dbManagerMock.getConnection()).thenReturn(connectionKeeperMock);
		when(dbManagerMock.getConnection().isConnected()).thenReturn(true);
		assertTrue(command.canProcess(singleCommand));
	}

	@Test
	public void process() throws SQLException, ExitException {
		commandLine = new String[] {"create","tbl","fld","int"};
		when(dbManagerMock.getConnection()).thenReturn(connectionKeeperMock);
		when(dbManagerMock.getConnection().isConnected()).thenReturn(true);
		doNothing().when(dbManagerMock).toCreate(any(DataContainer.class));
		assertEquals(CmdLineState.WAIT, command.process(commandLine));
	}

	@Test
	public void processWithSQLE() throws SQLException, ExitException {
		commandLine = new String[] {"create","tbl","fld","int"};
		when(dbManagerMock.getConnection()).thenReturn(connectionKeeperMock);
		when(dbManagerMock.getConnection().isConnected()).thenReturn(true);
		doThrow(SQLException.class).when(dbManagerMock).toCreate(any(DataContainer.class));
		assertEquals(CmdLineState.WAIT, command.process(commandLine));
	}

	@Test
	public void processNoTablename() throws SQLException, ExitException {
		commandLine = new String[] {"create"};
		when(dbManagerMock.getConnection()).thenReturn(connectionKeeperMock);
		when(dbManagerMock.getConnection().isConnected()).thenReturn(true);
		doNothing().when(dbManagerMock).toCreate(any(DataContainer.class));
		assertEquals(CmdLineState.WAIT, command.process(commandLine));
	}

	@Test
	public void processWrongCountArgs() throws SQLException, ExitException {
		commandLine = new String[] {"create","tbl","fld"};
		when(dbManagerMock.getConnection()).thenReturn(connectionKeeperMock);
		when(dbManagerMock.getConnection().isConnected()).thenReturn(true);
		doNothing().when(dbManagerMock).toCreate(any(DataContainer.class));
		assertEquals(CmdLineState.WAIT, command.process(commandLine));
	}

}