package ua.com.juja.yar_tur.sqlcmd.controller.commands;

import org.junit.*;
import ua.com.juja.yar_tur.sqlcmd.model.ConnectionKeeper;
import ua.com.juja.yar_tur.sqlcmd.model.DBCommandManager;
import ua.com.juja.yar_tur.sqlcmd.model.DataSet;
import ua.com.juja.yar_tur.sqlcmd.model.JDBCDatabaseManager;
import ua.com.juja.yar_tur.sqlcmd.types_enums_except.CmdLineState;
import ua.com.juja.yar_tur.sqlcmd.types_enums_except.FeedBack;
import ua.com.juja.yar_tur.sqlcmd.viewer.Console;
import ua.com.juja.yar_tur.sqlcmd.viewer.View;

import java.sql.SQLException;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DataInserterTest {
	private String singleCommand;
	private String[] commandLine;
	private DBCommandManager dbManagerMock = mock(JDBCDatabaseManager.class);
	private ConnectionKeeper connectionKeeperMock = mock(ConnectionKeeper.class);
	private View viewMock = mock(Console.class);
	private DataInserter command;

	@BeforeClass
	public static void setUpClass() throws Exception {
		System.out.println("Before DataInserterTest.class");
	}

	@AfterClass
	public static void tearDownClass() throws Exception {
		System.out.println("After DataInserterTest.class");
	}

	@Before
	public void setUp() throws Exception {
		singleCommand = "insert";
		command = new DataInserter(dbManagerMock,viewMock);
	}

	@After
	public void tearDown() throws Exception {
		singleCommand = null;
		commandLine = null;
		command = null;
	}

	@Test
	public void canProcess() {
		when(dbManagerMock.getConnection()).thenReturn(connectionKeeperMock);
		when(dbManagerMock.getConnection().isConnected()).thenReturn(true);
		assertEquals(true, command.canProcess(singleCommand));
	}

	@Test
	public void process() throws SQLException {
		commandLine = new String[]{"insert","tbl","fld","vly"};
		when(dbManagerMock.getConnection()).thenReturn(connectionKeeperMock);
		when(dbManagerMock.getConnection().isConnected()).thenReturn(true);
		when(dbManagerMock.toInsert(anyString(), any(DataSet.class))).
				thenReturn(FeedBack.OK);
		assertEquals(CmdLineState.WAIT, command.process(commandLine));
	}

	@Test
	public void processNoTableName() throws SQLException {
		commandLine = new String[]{"insert"};
		when(dbManagerMock.getConnection()).thenReturn(connectionKeeperMock);
		when(dbManagerMock.getConnection().isConnected()).thenReturn(true);
		when(dbManagerMock.toInsert(anyString(), any(DataSet.class))).
				thenReturn(FeedBack.OK);
		assertEquals(CmdLineState.WAIT, command.process(commandLine));
	}

	@Test
	public void processWithWrongCountArgs() throws SQLException {
		commandLine = new String[]{"insert","tbl","fld"};
		when(dbManagerMock.getConnection()).thenReturn(connectionKeeperMock);
		when(dbManagerMock.getConnection().isConnected()).thenReturn(true);
		when(dbManagerMock.toInsert(anyString(), any(DataSet.class))).
				thenReturn(FeedBack.OK);
		assertEquals(CmdLineState.WAIT, command.process(commandLine));
	}

	@Test
	public void processWithSQLE() throws SQLException {
		commandLine = new String[]{"insert","tbl","fld","vly"};
		when(dbManagerMock.getConnection()).thenReturn(connectionKeeperMock);
		when(dbManagerMock.getConnection().isConnected()).thenReturn(true);
		when(dbManagerMock.toInsert(anyString(), any(DataSet.class))).
				thenThrow(new SQLException());
		assertEquals(CmdLineState.WAIT, command.process(commandLine));
	}

	@Test
	public void processWrongInsertData() throws SQLException {
		commandLine = new String[]{"insert","tbl","fld"};
		when(dbManagerMock.getConnection()).thenReturn(connectionKeeperMock);
		when(dbManagerMock.getConnection().isConnected()).thenReturn(true);
		when(dbManagerMock.toInsert(anyString(), any(DataSet.class))).
				thenReturn(FeedBack.REFUSE);
		assertEquals(CmdLineState.WAIT, command.process(commandLine));
	}
}