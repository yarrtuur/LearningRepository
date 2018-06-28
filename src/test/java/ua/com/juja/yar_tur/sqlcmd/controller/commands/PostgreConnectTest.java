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

public class PostgreConnectTest {
    private String singleCommand;
    private String[] commandLine;
    private DBCommandManager dbManagerMock = mock(JDBCDatabaseManager.class);
    private ConnectionKeeper connectionKeeperMock = mock(ConnectionKeeper.class);
    private View viewMock = mock(Console.class);
    private PostgreConnect command;

    @BeforeClass
    public static void beforeClass() {
        System.out.println("Before PostgreConnectTest.class");
    }

    @AfterClass
    public static void afterClass() {
        System.out.println("After PostgreConnectTest.class");
    }


    @Before
    public void setUp() {
        command = new PostgreConnect(dbManagerMock, viewMock);
        singleCommand = "connect";
    }

    @After
    public void tearDown() {
        command = null;
        commandLine = null;
        singleCommand = null;
    }

    @Test
    public void canProcess() {
        assertTrue(command.canProcess(singleCommand));
    }

    @Test
    public void processNoConnectTest() throws SQLException {
        commandLine = new String[]{"connect"};
        doThrow(SQLException.class).when(dbManagerMock).toConnect(anyString(), anyString(), anyString());
        assertEquals(CmdLineState.WAIT, command.process(commandLine));
    }

    @Test
    public void processConnectProperties() throws SQLException {
        commandLine = new String[]{"connect"};
        when(dbManagerMock.getConnection()).thenReturn(connectionKeeperMock);
        doNothing().when(dbManagerMock).toConnect(anyString(), anyString(), anyString());
        assertEquals(CmdLineState.WAIT, command.process(commandLine));
    }
}