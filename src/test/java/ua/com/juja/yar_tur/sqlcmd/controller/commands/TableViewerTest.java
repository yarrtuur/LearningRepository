package ua.com.juja.yar_tur.sqlcmd.controller.commands;

import org.junit.*;
import ua.com.juja.yar_tur.sqlcmd.model.ConnectionKeeper;
import ua.com.juja.yar_tur.sqlcmd.model.DBCommandManager;
import ua.com.juja.yar_tur.sqlcmd.model.JDBCDatabaseManager;
import ua.com.juja.yar_tur.sqlcmd.utils.CmdLineState;
import ua.com.juja.yar_tur.sqlcmd.viewer.Console;
import ua.com.juja.yar_tur.sqlcmd.viewer.View;

import java.sql.SQLException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

public class TableViewerTest {
	private String singleCommand;
	private String[] commandLine;
	private DBCommandManager dbManagerMock = mock(JDBCDatabaseManager.class);
	private ConnectionKeeper connectionKeeperMock = mock(ConnectionKeeper.class);
	private View viewMock = mock(Console.class);
	private TableViewer command;

	@BeforeClass
    public static void setUpClass() {
		System.out.println("Before TableViewerTest.class");
	}

	@AfterClass
    public static void tearDownClass() {
		System.out.println("After TableViewerTest.class");
	}

	@Before
    public void setUp() {
		singleCommand = "tables";
		command = new TableViewer(dbManagerMock, viewMock);
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
		when(command.canProcess(singleCommand)).thenReturn(true);
		assertTrue(command.canProcess(singleCommand));
	}

	@Test
	public void processTablesListPrint() throws SQLException {
		commandLine = new String[]{"tables"};
		when(dbManagerMock.getConnection()).thenReturn(connectionKeeperMock);
		when(dbManagerMock.getConnection().isConnected()).thenReturn(true);
        doNothing().when(dbManagerMock).toView(anyString());
		assertEquals(CmdLineState.WAIT, command.process(commandLine));
	}

	@Test
	public void processOneTablePrint() throws SQLException {
		commandLine = new String[]{"tables","tbl"};
		when(dbManagerMock.getConnection()).thenReturn(connectionKeeperMock);
		when(dbManagerMock.getConnection().isConnected()).thenReturn(true);
        doNothing().when(dbManagerMock).toView(anyString());
        assertEquals(CmdLineState.WAIT, command.process(commandLine));
    }

    @Test
    public void processOneTablePrintWithSQLE() throws SQLException {
        commandLine = new String[]{"tables", "tbl"};
        when(dbManagerMock.getConnection()).thenReturn(connectionKeeperMock);
        when(dbManagerMock.getConnection().isConnected()).thenReturn(true);
        doThrow(SQLException.class).when(dbManagerMock).toView(anyString());
		assertEquals(CmdLineState.WAIT, command.process(commandLine));
	}

	@Test
	public void processWithSQLE() throws SQLException {
		commandLine = new String[]{"tables"};
		when(dbManagerMock.getConnection()).thenReturn(connectionKeeperMock);
		when(dbManagerMock.getConnection().isConnected()).thenReturn(true);
        doThrow(SQLException.class).when(dbManagerMock).toView(anyString());
		assertEquals(CmdLineState.WAIT, command.process(commandLine));
	}

}
