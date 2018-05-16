package ua.com.juja.yar_tur.sqlcmd.controller;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ua.com.juja.yar_tur.sqlcmd.model.DBCommandManager;
import ua.com.juja.yar_tur.sqlcmd.model.JDBCDatabaseManager;
import ua.com.juja.yar_tur.sqlcmd.types_enums_except.CmdLineState;
import ua.com.juja.yar_tur.sqlcmd.viewer.Console;
import ua.com.juja.yar_tur.sqlcmd.viewer.View;

import java.sql.SQLException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TableCreaterTest {
    private CommandProcess command;
    private DBCommandManager dbManager;
    private View view;
    private String singleCommand;
    private String[] commandLine;

    @Before
    public void setUp() throws SQLException {
        dbManager = new JDBCDatabaseManager();
        view = new Console();
        command = new TableCreater(dbManager, view);
        dbManager.toConnect("jdbc:postgresql://127.0.0.1:5432/postgres", "postgres", "1");
        singleCommand = "create | clone | fld | integer ";
        commandLine = singleCommand.replaceAll("\\s", "").toLowerCase().split("\\|");
        System.out.println("Start of TableCreaterTest");
    }

    @After
    public void tearDown() throws SQLException {
        dbManager.toExit();
        dbManager = null;
        command = null;
        System.out.println("End of TableCreaterTest");
    }

    @Test
    public void canProcess() {
        boolean canProcess = command.canProcess("create");
        assertTrue(canProcess);
    }

    @Test
    public void process() {
        assertEquals(CmdLineState.WAIT, command.process(commandLine));
    }

    @Test
    public void processWrongTable() {
        singleCommand = "create |  ";
        commandLine = singleCommand.replaceAll("\\s", "").toLowerCase().split("\\|");
        assertEquals(CmdLineState.WAIT, command.process(commandLine));
    }

    @Test
    public void processWrongCountArguments() {
        singleCommand = "create | clone | fld ";
        commandLine = singleCommand.replaceAll("\\s", "").toLowerCase().split("\\|");
        assertEquals(CmdLineState.WAIT, command.process(commandLine));
    }
}