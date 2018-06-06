package ua.com.juja.yar_tur.sqlcmd.controller.commands;

import org.junit.*;
import ua.com.juja.yar_tur.sqlcmd.model.ConnectionKeeper;
import ua.com.juja.yar_tur.sqlcmd.model.DBCommandManager;
import ua.com.juja.yar_tur.sqlcmd.types_enums_except.CmdLineState;
import ua.com.juja.yar_tur.sqlcmd.viewer.Console;
import ua.com.juja.yar_tur.sqlcmd.viewer.View;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ChkConnectionTest {
    DBCommandManager dbManagerMock = mock(DBCommandManager.class);
    View viewMock = mock(Console.class);
    ConnectionKeeper connectionKeeperMock = mock(ConnectionKeeper.class);
    ChkConnection command;
    String singleCommand;
    String[] commandLine = {"chkconn"};

    @BeforeClass
    public static void setUpClass()  {
        System.out.println("Before ChkConnectionTest.class");
    }

    @AfterClass
    public static void tearDownClass(){
        System.out.println("After ChkConnectionTest.class");
    }

    @Before
    public void setUp() throws Exception {
        command = new ChkConnection(dbManagerMock, viewMock);
        singleCommand = "chkconn";
    }

    @After
    public void tearDown() throws Exception {
        command = null;
        singleCommand = null;
        commandLine = null;
    }

    @Test
    public void canProcess() {
        assertEquals(true, command.canProcess(singleCommand));
    }

    @Test
    public void processWithConn() {
        when(dbManagerMock.getConnection()).thenReturn(connectionKeeperMock);
        when(dbManagerMock.getConnection().isConnected()).thenReturn(true);
        assertEquals(CmdLineState.WAIT, command.process(commandLine));
    }

    @Test
    public void processWithoutConn() {
        when(dbManagerMock.getConnection()).thenReturn(connectionKeeperMock);
        when(dbManagerMock.getConnection().isConnected()).thenReturn(false);
        assertEquals(CmdLineState.WAIT, command.process(commandLine));
    }
}