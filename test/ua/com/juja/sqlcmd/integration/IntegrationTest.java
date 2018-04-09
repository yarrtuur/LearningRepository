package ua.com.juja.sqlcmd.integration;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ua.com.juja.sqlcmd.model.MainController;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;

public class IntegrationTest {
    private static ConfigurableInputStream in;
    private static ByteArrayOutputStream out;

    @Before
    public void setup(){

        out = new ByteArrayOutputStream ();
        in = new ConfigurableInputStream ();
        System.setIn ( in );
        System.setOut ( new PrintStream ( out ) );
    }

    @Test
    public void testHelp(){
        // given
        in.add ( "help" );
        // when
        MainController.getInstance ().mainDialogueHolder ();

        // then
        Assert.assertEquals("Hello, user!\r\n" +
                "Please, set the connect string by format: \r\n" +
                "connect | login | password | database \r\n" +
                " or type `help` for list available commands. \r\n" +
                "List of commands:\r\n" +
                " exit : exit\r\n" +
                " help : help\r\n" +
                " connect : connect | username | password | database_sid | <IP-addr> | <port> \r\n" +
                " tables : tables OR tables | fields \r\n" +
                " clear : clear | tableName \r\n" +
                " drop : drop | tableName \r\n" +
                " create : create | tableName | column1 | column2 | ... | columnN\r\n" +
                " find : find | tableName OR  find | tableName | column | value\r\n" +
                " insert : insert | tableName | column1 | value1 | column2 | value2 | ... | columnN | valueN\r\n"
                , getData());
    }

    @Test
    public void testExit(){
        // given
        in.add ( "exit" );
        // when
        MainController.getInstance ().mainDialogueHolder ();
        // then
        Assert.assertEquals ( "Hello, user!\r\n" +
                "Please, set the connect string by format: \r\n" +
                "connect | login | password | database \r\n" +
                " or type `help` for list available commands. \r\n" +
                "You are disconnected now. Bye...\r\n", getData () );
    }

    public String getData() {
        try {
            return new String ( out.toByteArray (), "UTF-8" );
        } catch (UnsupportedEncodingException e) {
            return e.getMessage ();
        }
    }
}
