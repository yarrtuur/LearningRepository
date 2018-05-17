package ua.com.juja.yar_tur.sqlcmd.controller.commands;

import org.junit.*;
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

public class TableViewerTest {
    private CommandProcess command;
    private DBCommandManager dbManager;
    private View view;
    private String singleCommand;
    private String[] commandLine;

    @BeforeClass
    public static void setUpClass() throws Exception {
        System.out.println("Start TableViewerTest");
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
        System.out.println("Finish TableViewerTest");
    }

    @Before
    public void setUp() throws SQLException {
        dbManager = new JDBCDatabaseManager();
        view = new Console();
        command = new TableViewer(dbManager, view);
        dbManager.toConnect("jdbc:postgresql://127.0.0.1:5432/postgres", "postgres", "1");
        DataSet dataSet = new DataSet();
        dataSet.add("fld", "integer");
        dbManager.toCreate("clone", dataSet);
        System.out.println("Start of TableViewerTest");
    }

    @After
    public void tearDown() throws SQLException {
        dbManager.toDrop("clone");
        dbManager.toExit();
        dbManager = null;
        command = null;
        System.out.println("End of TableViewerTest");
    }

    @Test
    public void canProcess() {
        boolean canProcess = command.canProcess("tables");
        assertTrue(canProcess);
    }

    @Test
    public void processAllTables() {
        singleCommand = "tables";
        commandLine = singleCommand.replaceAll("\\s", "").toLowerCase().split("\\|");
        assertEquals(CmdLineState.WAIT, command.process(commandLine));
    }

    @Test
    public void processTablesDetails() {
        singleCommand = "tables | fields ";
        commandLine = singleCommand.replaceAll("\\s", "").toLowerCase().split("\\|");
        assertEquals(CmdLineState.WAIT, command.process(commandLine));
    }

    @Test
    public void processOneTable() {
        singleCommand = "tables | clone ";
        commandLine = singleCommand.replaceAll("\\s", "").toLowerCase().split("\\|");
        assertEquals(CmdLineState.WAIT, command.process(commandLine));
    }
}