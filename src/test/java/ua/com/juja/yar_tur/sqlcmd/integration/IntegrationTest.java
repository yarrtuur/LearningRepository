package ua.com.juja.yar_tur.sqlcmd.integration;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ua.com.juja.yar_tur.sqlcmd.model.Main;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;

public class IntegrationTest {
    private static ConfigurableInputStream in;
    private static ByteArrayOutputStream out;
    private final String CARET = System.getProperty ( "line.separator" );

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
        new Main();
        // then
        Assert.assertEquals("Hello, user!" +CARET+
                "Please, type `help` for list available commands. " +CARET+
                "List of commands:" +CARET+
                " exit : exit" +CARET+
                " help : help" +CARET+
                " connect : connect | username | password | database_sid | <IP-addr> | <port> " +CARET+
                " create : create | tableName | column1 | type1 | ... | columnN " +CARET+
                " tables : tables OR tables | fields  OR tables | tableName " +CARET+
                " insert : insert | tableName | column1 | value1 | column2 | value2 | ... | columnN | valueN " +CARET +
                " find : find | tableName OR  find | tableName | column | value " +CARET+
                " clear : clear | tableName " +CARET+
                " drop : drop | tableName " +CARET+
                " delete : delete | tableName | column | value " +CARET+
                " update : update | tableName | SET | column1 | value1 | column2 | value2 | WHERE| columnX | valueX "+CARET
                , getData());
    }

    @Test
    public void testExit(){
        // given
        in.add ( "exit" );
        // when
        new Main();
        // then
        Assert.assertEquals ( "Hello, user!" + CARET +
                "Please, type `help` for list available commands. " + CARET +
                "Closing connection..." + CARET +
                "Connection closed." + CARET, getData () );
    }

    public String getData() {
        try {
            return new String ( out.toByteArray (), "UTF-8" );
        } catch (UnsupportedEncodingException e) {
            return e.getMessage ();
        }
    }
}
