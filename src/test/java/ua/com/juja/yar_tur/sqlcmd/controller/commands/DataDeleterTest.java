package ua.com.juja.yar_tur.sqlcmd.controller.commands;

import org.junit.*;
import ua.com.juja.yar_tur.sqlcmd.controller.commands.DataDeleter;
import ua.com.juja.yar_tur.sqlcmd.model.CommandProcess;
import ua.com.juja.yar_tur.sqlcmd.model.DBCommandManager;
import ua.com.juja.yar_tur.sqlcmd.model.DataSet;
import ua.com.juja.yar_tur.sqlcmd.model.JDBCDatabaseManager;
import ua.com.juja.yar_tur.sqlcmd.types_enums_except.CmdLineState;
import ua.com.juja.yar_tur.sqlcmd.viewer.Console;
import ua.com.juja.yar_tur.sqlcmd.viewer.View;

import java.sql.SQLException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class DataDeleterTest {
    private CommandProcess command ;
    private DBCommandManager dbManager;
    private View view;
    private String singleCommand;
    private String[] commandLine;

    @BeforeClass
    public static void setUpClass() throws Exception {
        System.out.println("Start DataDeleterTest");
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
        System.out.println("Finish DataDeleterTest");
    }

    @Before
    public void setUp() throws SQLException {
        dbManager = new JDBCDatabaseManager();
        view = new Console();
        command = new DataDeleter( dbManager, view);
        dbManager.toConnect ( "jdbc:postgresql://127.0.0.1:5432/postgres", "postgres", "1" );
        DataSet dataSet = new DataSet();
        dataSet.add ( "fld", "integer" );
        dbManager.toCreate ( "clone", dataSet);
        dataSet = new DataSet ();
        dataSet.add ( "fld", "1" );
        dbManager.toInsert ( "clone", dataSet);
        System.out.println ("Start of DataDeleterTest");
    }

    @After
    public void tearDown() throws Exception {
        dbManager.toDrop ( "clone" );
        dbManager.toExit ();
        dbManager = null;
        command = null;
        System.out.println ("End of DataDeleterTest");
    }

    @Test
    public void canProcess() {
        boolean canProcess = command.canProcess ( "delete" );
        assertTrue ( canProcess );
    }

    @Test
    public void process() {
        singleCommand = "delete | clone | fld | 1";
        commandLine = singleCommand.replaceAll("\\s", "").toLowerCase().split("\\|");
        assertEquals ( CmdLineState.WAIT, command.process (commandLine ) );
    }
    @Test
    public void processDataWrong() {
        singleCommand = "delete | clone | fld | ";
        commandLine = singleCommand.replaceAll("\\s", "").toLowerCase().split("\\|");
        assertEquals ( CmdLineState.WAIT, command.process ( commandLine ) );
    }
    @Test
    public void processTableWrong() {
        singleCommand = "delete | ";
        commandLine = singleCommand.replaceAll("\\s", "").toLowerCase().split("\\|");
        assertEquals ( CmdLineState.WAIT, command.process ( commandLine ) );
    }
}