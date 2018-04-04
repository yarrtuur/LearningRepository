package ua.com.juja.sqlcmd.integration;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ua.com.juja.sqlcmd.controller.MainDialogue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;

public class IntegrationTest {
    private static ConfigurableInputStream in;
    private static ByteArrayOutputStream out;
    private String caret;

    @Before
    public void setup() {

        out = new ByteArrayOutputStream ();
        in = new ConfigurableInputStream ();
        System.setIn ( in );
        System.setOut ( new PrintStream ( out ) );
        caret = System.getProperty ( "line.separator" );
    }

    @Test
    public void testHelp() {
        // given
        in.add ( "help" );
        // when
        MainDialogue.getInstance ().mainDialogueHolder ();

        // then
        Assert.assertEquals ( "Hello, user!" + caret +
                        "Please, set the connect string by format: " + caret +
                        "connect | login | password | database " + caret +
                        " or type `help` for list available commands. " + caret +
                        "List of commands:" + caret +
                        " exit : exit" + caret +
                        " help : help" + caret +
                        " connect : connect | username | password | database_sid | <IP-addr> | <port> " + caret +
                        " tables : tables OR tables | fields " + caret +
                        " clear : clear | tableName " + caret +
                        " drop : drop | tableName " + caret +
                        " create : create | tableName | column1 | column2 | ... | columnN" + caret +
                        " find : find | tableName OR  find | tableName | column | value" + caret +
                        " insert : insert | tableName | column1 | value1 | column2 | value2 | ... | columnN | valueN" + caret
                , getData () );
    }

    @Test
    public void testExit() {
        // given
        in.add ( "exit" );
        // when
        MainDialogue.getInstance ().mainDialogueHolder ();
        // then
        Assert.assertEquals ( "Hello, user!" + caret +
                "Please, set the connect string by format: " + caret +
                "connect | login | password | database " + caret +
                " or type `help` for list available commands. " + caret +
                "You are disconnected now. Bye..." + caret, getData () );
    }

    @Test
    public void testWithoutConnect() {
        // given
        in.add ( "drop | table" );
        in.add ( "tables" );
        in.add ( "find | tableName" );
        in.add ( "create | tableName" );
        in.add ( "clear | tableName" );
        in.add ( "command " );
        in.add ( "exit " );

        // when
        MainDialogue.getInstance ().mainDialogueHolder ();
        // then
        Assert.assertEquals ( "Hello, user!" + caret +
                        "Please, set the connect string by format: " + caret +
                        "connect | login | password | database " + caret +
                        " or type `help` for list available commands. " + caret +
                        //drop
                        "Not connected to DB." + caret +
                        "REFUSE" + caret +
                        //tables
                        "Not connected to DB." + caret +
                        "REFUSE" + caret +
                        //find
                        "Not connected to DB." + caret +
                        "REFUSE" + caret +
                        //create
                        "Not connected to DB." + caret +
                        "REFUSE" + caret +
                        //clear
                        "Not connected to DB." + caret +
                        "REFUSE" + caret +
                        //command
                        "Not available command. Please type `help` to list all commands " + caret +
                        //exit
                        "You are disconnected now. Bye..." + caret
                , getData () );
    }

    @Test
    public void testCreateCreateViewWrongDropWithConnect() {
        // given
        in.add ( "connect|postgres|1|postgres" );
        in.add ( "create|integratetable|descr" );
        in.add ( "tables | fields" );
        in.add ( "create|integratetable|descr" );
        in.add ( "drop|integratetable" );
        in.add ( "exit" );

        // when
        MainDialogue.getInstance ().mainDialogueHolder ();

        // then
        Assert.assertEquals ( "Hello, user!" + caret +
                        "Please, set the connect string by format: " + caret +
                        "connect | login | password | database " + caret +
                        " or type `help` for list available commands. " + caret +
                        //connect
                        "Starting connect..." + caret +
                        "-------- PostgreSQL JDBC Connection Testing ------------" + caret +
                        "PostgreSQL JDBC Driver Registered!" + caret +
                        "You made it, take control your database now!" + caret +
                        "OK\r\n" +
                        //create
                        "Creating table in given database..." + caret +
                        "CREATE TABLE Query returned successfully" + caret +
                        "OK" + caret +
                        //view
                        "Selecting tables from a schema in given database..." + caret +
                        "Table: integratetable , Columns:  rid, descr " + caret +
                        "OK" + caret +
                        //create 2
                        "Table integratetable already exists." + caret +
                        "REFUSE" + caret +
                        //drop
                        "Droping table from given database..." + caret +
                        "table deleted successfully" + caret +
                        "OK" + caret +
                        //exit
                        "You are disconnected now. Bye..." + caret
                , getData () );
    }

    @Test
    public void testInsertWrongCreateInsertDropWithConnect() {
        // given
        in.add ( "connect|postgres|1|postgres" );
        in.add ( "insert|integratetable|descr|5" );
        in.add ( "create|integratetable|descr" );
        in.add ( "insert|integratetable|descr|5" );
        in.add ( "drop|integratetable" );
        in.add ( "exit" );

        // when
        MainDialogue.getInstance ().mainDialogueHolder ();

        // then
        Assert.assertEquals ( "Hello, user!" + caret +
                        "Please, set the connect string by format: " + caret +
                        "connect | login | password | database " + caret +
                        " or type `help` for list available commands. " + caret +
                        //connect
                        "Starting connect..." + caret +
                        "-------- PostgreSQL JDBC Connection Testing ------------" + caret +
                        "PostgreSQL JDBC Driver Registered!" + caret +
                        "You made it, take control your database now!" + caret +
                        "OK" + caret +
                        //insert
                        "Inserting data to table in given database..." + caret +
                        "insert data into  table is interrupted in given database..." + caret +
                        "REFUSE" + caret +
                        //create
                        "Creating table in given database..." + caret +
                        "CREATE TABLE Query returned successfully" + caret +
                        "OK" + caret +
                        //create 2
                        "Inserting data to table in given database..." + caret +
                        "insert data into table is done " + caret +
                        "OK" + caret +
                        //drop
                        "Droping table from given database..." + caret +
                        "table deleted successfully" + caret +
                        "OK" + caret +
                        //exit
                        "You are disconnected now. Bye..." + caret
                , getData () );
    }

    @Test
    public void testFinderWrongCreateFindInsertDataFinderDropWithConnect() {
        // given
        in.add ( "connect|postgres|1|postgres" );
        in.add ( "find | integratetable " );
        in.add ( "create|integratetable|descr" );
        in.add ( "find | integratetable " );
        in.add ( "insert|integratetable|descr|5" );
        in.add ( "find | integratetable | descr | 5 " );
        in.add ( "drop |integratetable" );
        in.add ( "exit" );

        // when
        MainDialogue.getInstance ().mainDialogueHolder ();

        // then
        Assert.assertEquals ( "Hello, user!" + caret +
                        "Please, set the connect string by format: " + caret +
                        "connect | login | password | database " + caret +
                        " or type `help` for list available commands. " + caret +
                        //connect
                        "Starting connect..." + caret +
                        "-------- PostgreSQL JDBC Connection Testing ------------" + caret +
                        "PostgreSQL JDBC Driver Registered!" + caret +
                        "You made it, take control your database now!" + caret +
                        "OK" + caret +
                        //find
                        "Selecting data from table in given database..." + caret +
                        "ERROR: table does not exists" + caret +
                        "REFUSE" + caret +
                        //create
                        "Creating table in given database..." + caret +
                        "CREATE TABLE Query returned successfully" + caret +
                        "OK" + caret +
                        //find
                        "Selecting data from table in given database..." + caret +
                        " | rid | descr | " + caret +
                        "OK" + caret +
                        //insert
                        "Inserting data to table in given database..." + caret +
                        "insert data into table is done " + caret +
                        "OK" + caret +
                        //find column
                        "Selecting data from table in given database..." + caret +
                        " | rid | descr | " + caret +
                        " | 1 | 5 | " + caret +
                        "OK" + caret +
                        //drop
                        "Droping table from given database..." + caret +
                        "table deleted successfully" + caret +
                        "OK" + caret +
                        //exit
                        "You are disconnected now. Bye..." + caret
                , getData () );
    }

    @Test
    public void testCleanerWrongCreateInsertFinderCleanerFinderDropWithConnect() {
        // given
        in.add ( "connect|postgres|1|postgres" );
        in.add ( "clear | integratetable " );
        in.add ( "create|integratetable|descr" );
        in.add ( "insert|integratetable|descr|5" );
        in.add ( "find | integratetable " );
        in.add ( "clear | integratetable " );
        in.add ( "find | integratetable " );
        in.add ( "drop |integratetable" );
        in.add ( "exit" );

        // when
        MainDialogue.getInstance ().mainDialogueHolder ();

        // then
        Assert.assertEquals ( "Hello, user!" + caret +
                        "Please, set the connect string by format: " + caret +
                        "connect | login | password | database " + caret +
                        " or type `help` for list available commands. " + caret +
                        //connect
                        "Starting connect..." + caret +
                        "-------- PostgreSQL JDBC Connection Testing ------------" + caret +
                        "PostgreSQL JDBC Driver Registered!" + caret +
                        "You made it, take control your database now!" + caret +
                        "OK" + caret +
                        //clear
                        "Deleting data from table in given database..." + caret +
                        "ERROR: table does not exists" + caret +
                        "REFUSE" + caret +
                        //create
                        "Creating table in given database..." + caret +
                        "CREATE TABLE Query returned successfully" + caret +
                        "OK" + caret +
                        //insert
                        "Inserting data to table in given database..." + caret +
                        "insert data into table is done " + caret +
                        "OK" + caret +
                        //find column
                        "Selecting data from table in given database..." + caret +
                        " | rid | descr | " + caret +
                        " | 1 | 5 | " + caret +
                        "OK" + caret +
                        //clear
                        "Deleting data from table in given database..." + caret +
                        "data deleted successfully" + caret +
                        "OK" + caret +
                        //find
                        "Selecting data from table in given database..." + caret +
                        " | rid | descr | " + caret +
                        "OK" + caret +
                        //drop
                        "Droping table from given database..." + caret +
                        "table deleted successfully" + caret +
                        "OK" + caret +
                        //exit
                        "You are disconnected now. Bye..." + caret
                , getData () );
    }

    private String getData() {
        try {
            return new String ( out.toByteArray (), "UTF-8" );
        } catch (UnsupportedEncodingException e) {
            return e.getMessage ();
        }
    }
}
