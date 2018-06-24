package ua.com.juja.yar_tur.sqlcmd.controller.commands;

import org.junit.*;
import ua.com.juja.yar_tur.sqlcmd.model.ConnectionKeeper;
import ua.com.juja.yar_tur.sqlcmd.model.DBCommandManager;
import ua.com.juja.yar_tur.sqlcmd.utils.CmdLineState;
import ua.com.juja.yar_tur.sqlcmd.viewer.Console;
import ua.com.juja.yar_tur.sqlcmd.viewer.View;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ChkConnectionTest {
    private DBCommandManager dbManagerMock = mock(DBCommandManager.class);
    private View viewMock = mock(Console.class);
    private ConnectionKeeper connectionKeeperMock = mock(ConnectionKeeper.class);
    private ChkConnection command;
    private String singleCommand;
    private String[] commandLine = {"chkconn"};

    @BeforeClass
    public static void setUpClass()  {
        System.out.println("Before ChkConnectionTest.class");
    }

    @AfterClass
    public static void tearDownClass(){
        System.out.println("After ChkConnectionTest.class");
    }

    @Before
	public void setUp() {
        command = new ChkConnection(dbManagerMock, viewMock);
        singleCommand = "chkconn";
    }

    @After
	public void tearDown() {
        command = null;
        singleCommand = null;
        commandLine = null;
    }

    @Test
    public void canProcess() {
		assertTrue(command.canProcess(singleCommand));
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