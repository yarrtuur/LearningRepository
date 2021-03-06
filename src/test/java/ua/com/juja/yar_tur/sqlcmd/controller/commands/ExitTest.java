package ua.com.juja.yar_tur.sqlcmd.controller.commands;

import org.junit.*;
import ua.com.juja.yar_tur.sqlcmd.model.CommandProcess;
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

public class ExitTest {

	private String[] commandLine;
	private String singleCommand;
	private DBCommandManager dbManagerMock = mock(JDBCDatabaseManager.class);
	private ConnectionKeeper connectionKeeperMock = mock(ConnectionKeeper.class);
	private View viewMock = mock(Console.class);
	private CommandProcess command;


	@BeforeClass
	public static void beforeClass() {
		System.out.println("Before ExitTest.class");
	}

	@AfterClass
	public static void afterClass() {
		System.out.println("After ExitTest.class");
	}

	@Before
	public void setUp() {
		command = new Exit(dbManagerMock, viewMock);
		commandLine = new String[]{"exit"};
		singleCommand = "exit";
	}

	@After
	public void tearDown() {
		command = null;
		commandLine = null;
		singleCommand = null;
	}

	@Test
	public void processNoConnectTest() throws Exception {
		when(dbManagerMock.getConnection()).thenReturn(connectionKeeperMock);
		doThrow(SQLException.class).when(dbManagerMock).toExit();
		assertEquals(CmdLineState.EXIT, command.process(commandLine));
	}

	@Test
	public void processTest() throws Exception {
		doNothing().when(dbManagerMock).toExit();
		assertEquals(CmdLineState.EXIT, command.process(commandLine));
	}

	@Test
	public void canProcessTest() {
		assertTrue(command.canProcess(singleCommand));
	}

}