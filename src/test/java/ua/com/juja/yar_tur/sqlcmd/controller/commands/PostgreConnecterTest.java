package ua.com.juja.yar_tur.sqlcmd.controller.commands;

import org.junit.*;
import ua.com.juja.yar_tur.sqlcmd.model.DBCommandManager;
import ua.com.juja.yar_tur.sqlcmd.model.JDBCDatabaseManager;
import ua.com.juja.yar_tur.sqlcmd.types_enums_except.CmdLineState;
import ua.com.juja.yar_tur.sqlcmd.viewer.Console;
import ua.com.juja.yar_tur.sqlcmd.viewer.View;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class PostgreConnecterTest {
    private PostgreConnect command;
    private DBCommandManager dbManager;
    private View view;
    private String singleCommand;
    private String[] commandLine;

    @BeforeClass
    public static void setUpClass() throws Exception {
        System.out.println("Start PostgreConnecterTest");
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
        System.out.println("Finish PostgreConnecterTest");
    }

    @Before
    public void setUp() {
        dbManager = new JDBCDatabaseManager();
        view = new Console();
        command = new PostgreConnect(dbManager, view);
        singleCommand = "connect | postgres | 1 | postgres | 127.0.0.1 | 5432";
        commandLine = singleCommand.replaceAll("\\s", "").toLowerCase().split("\\|");

        System.out.println ("Start of PostgreConnecterTest");
    }

    @After
    public void tearDown() {
        dbManager = null;
        command = null;
        System.out.println ("End of PostgreConnecterTest");
    }

    @Test
    public void canProcess() {
        boolean canProcess = command.canProcess ( commandLine[0] );
        assertTrue ( canProcess );
    }

    @Test
    public void process() {
        assertEquals ( CmdLineState.WAIT, command.process ( commandLine ) );
    }

}