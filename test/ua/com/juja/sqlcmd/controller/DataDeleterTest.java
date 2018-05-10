package ua.com.juja.sqlcmd.controller;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ua.com.juja.sqlcmd.model.DataSet;
import ua.com.juja.sqlcmd.types_enums_except.CmdLineState;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class DataDeleterTest {
    private CommandProcessable command ;
    private DBCommandManager dbManager;
    private String singleCommand;
    private String[] commandLine;

    @Before
    public void setUp() throws Exception {
        command = new DataDeleter ();
        dbManager = new DBCommandManager ();
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
        assertEquals ( CmdLineState.WAIT, command.process ( dbManager, commandLine ) );
    }
    @Test
    public void processDataWrong() {
        singleCommand = "delete | clone | fld | ";
        commandLine = singleCommand.replaceAll("\\s", "").toLowerCase().split("\\|");
        assertEquals ( CmdLineState.WAIT, command.process ( dbManager, commandLine ) );
    }
    @Test
    public void processTableWrong() {
        singleCommand = "delete | ";
        commandLine = singleCommand.replaceAll("\\s", "").toLowerCase().split("\\|");
        assertEquals ( CmdLineState.WAIT, command.process ( dbManager, commandLine ) );
    }
}