package maven_sql.sqlCmd;

import maven_sql.sqlCmd.modules.CmdLineParser;
import maven_sql.sqlCmd.types_enums.CmdLineState;
import org.junit.Test;

import static org.junit.Assert.*;

public class CmdLineParserTest {

    @Test
    public void testTakeUpCmdLine() {
        CmdLineParser clp = new CmdLineParser();
        assertEquals(CmdLineState.WAIT, clp.getCMDState());
        clp.takeUpCmdLine("exit");
        assertEquals(CmdLineState.EXIT, clp.getCMDState());
    }
}