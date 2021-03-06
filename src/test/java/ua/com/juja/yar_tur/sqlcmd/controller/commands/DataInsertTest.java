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
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DataInsertTest {
	private String singleCommand;
	private String[] commandLine;
	private DBCommandManager dbManagerMock = mock(JDBCDatabaseManager.class);
	private ConnectionKeeper connectionKeeperMock = mock(ConnectionKeeper.class);
	private View viewMock = mock(Console.class);
	private DataInsert command;

	@BeforeClass
	public static void setUpClass() {
		System.out.println("Before DataInsertTest.class");
	}

	@AfterClass
	public static void tearDownClass() {
		System.out.println("After DataInsertTest.class");
	}

	@Before
	public void setUp() {
		singleCommand = "insert";
		command = new DataInsert(dbManagerMock, viewMock);
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
		commandLine = new String[]{"insert","tbl","fld","vly"};
		when(dbManagerMock.getConnection()).thenReturn(connectionKeeperMock);
		when(dbManagerMock.getConnection().isConnected()).thenReturn(true);
		when(dbManagerMock.toInsert(any(DataContainer.class))).thenReturn(anyInt());
		assertEquals(CmdLineState.WAIT, command.process(commandLine));
	}

	@Test
	public void processNoTableName() throws SQLException {
		commandLine = new String[]{"insert"};
		when(dbManagerMock.getConnection()).thenReturn(connectionKeeperMock);
		when(dbManagerMock.getConnection().isConnected()).thenReturn(true);
		when(dbManagerMock.toInsert(any(DataContainer.class))).thenReturn(anyInt());
		assertEquals(CmdLineState.WAIT, command.process(commandLine));
	}

	@Test
	public void processWithWrongCountArgs() throws SQLException {
		commandLine = new String[]{"insert","tbl","fld"};
		when(dbManagerMock.getConnection()).thenReturn(connectionKeeperMock);
		when(dbManagerMock.getConnection().isConnected()).thenReturn(true);
		when(dbManagerMock.toInsert(any(DataContainer.class))).thenReturn(anyInt());
		assertEquals(CmdLineState.WAIT, command.process(commandLine));
	}

	@Test
	public void processWithSQLE() throws SQLException {
		commandLine = new String[]{"insert","tbl","fld","vly"};
		when(dbManagerMock.getConnection()).thenReturn(connectionKeeperMock);
		when(dbManagerMock.getConnection().isConnected()).thenReturn(true);
		when(dbManagerMock.toInsert(any(DataContainer.class))).thenThrow(new SQLException());
		assertEquals(CmdLineState.WAIT, command.process(commandLine));
	}

	@Test
	public void processWrongInsertData() throws SQLException {
		commandLine = new String[]{"insert","tbl","fld"};
		when(dbManagerMock.getConnection()).thenReturn(connectionKeeperMock);
		when(dbManagerMock.getConnection().isConnected()).thenReturn(true);
		when(dbManagerMock.toInsert(any(DataContainer.class))).thenReturn(anyInt());
		assertEquals(CmdLineState.WAIT, command.process(commandLine));
	}
}