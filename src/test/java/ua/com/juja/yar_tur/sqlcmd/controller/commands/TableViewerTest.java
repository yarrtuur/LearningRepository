package ua.com.juja.yar_tur.sqlcmd.controller.commands;

import org.junit.*;
import ua.com.juja.yar_tur.sqlcmd.model.ConnectionKeeper;
import ua.com.juja.yar_tur.sqlcmd.model.DBCommandManager;
import ua.com.juja.yar_tur.sqlcmd.model.JDBCDatabaseManager;
import ua.com.juja.yar_tur.sqlcmd.types_enums_except.CmdLineState;
import ua.com.juja.yar_tur.sqlcmd.types_enums_except.FeedBack;
import ua.com.juja.yar_tur.sqlcmd.viewer.Console;
import ua.com.juja.yar_tur.sqlcmd.viewer.View;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TableViewerTest {
	private String singleCommand;
	private String[] commandLine;
	private DBCommandManager dbManagerMock = mock(JDBCDatabaseManager.class);
	private ConnectionKeeper connectionKeeperMock = mock(ConnectionKeeper.class);
	private View viewMock = mock(Console.class);
	private ResultSet resultSetMock = mock(ResultSet.class);
	private TableViewer command;// = mock(TableViewer.class);

	@BeforeClass
	public static void setUpClass() throws Exception {
		System.out.println("Before TableViewerTest.class");
	}

	@AfterClass
	public static void tearDownClass() throws Exception {
		System.out.println("After TableViewerTest.class");
	}

	@Before
	public void setUp() throws Exception {
		singleCommand = "tables";
		command = new TableViewer(dbManagerMock, viewMock);
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
		when(command.canProcess(singleCommand)).thenReturn(true);
		assertEquals(true, command.canProcess(singleCommand));
	}

	@Test
	public void processTablesListPrint() throws SQLException {
		commandLine = new String[]{"tables"};
		when(dbManagerMock.getConnection()).thenReturn(connectionKeeperMock);
		when(dbManagerMock.getConnection().isConnected()).thenReturn(true);
		when(dbManagerMock.toView()).thenReturn(FeedBack.OK);
		assertEquals(CmdLineState.WAIT, command.process(commandLine));
	}

	@Test
	public void processOneTablePrint() throws SQLException {
		commandLine = new String[]{"tables","tbl"};
		when(dbManagerMock.getConnection()).thenReturn(connectionKeeperMock);
		when(dbManagerMock.getConnection().isConnected()).thenReturn(true);
		when(dbManagerMock.toView(anyString())).thenReturn(FeedBack.OK);
		assertEquals(CmdLineState.WAIT, command.process(commandLine));
	}

	@Test
	public void processSQLE() throws SQLException {
		commandLine = new String[]{"tables"};
		when(dbManagerMock.getConnection()).thenReturn(connectionKeeperMock);
		when(dbManagerMock.getConnection().isConnected()).thenReturn(true);
		when(dbManagerMock.toView()).thenThrow(new SQLException());
		assertEquals(CmdLineState.WAIT, command.process(commandLine));
	}

}
