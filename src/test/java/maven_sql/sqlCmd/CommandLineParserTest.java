package maven_sql.sqlCmd;

import org.junit.Test;

import static org.junit.Assert.*;

public class CommandLineParserTest {

    @Test
    public void testTakeUpCmdLine() {
        CommandLineParser clp = new CommandLineParser();
        assertEquals(CommandLineState.WAIT, clp.getCMDState());
        clp.takeUpCmdLine("exit");
        assertEquals(CommandLineState.EXIT, clp.getCMDState());
    }
}