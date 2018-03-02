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
        clp.takeUpCmdLine("help");
        assertEquals(CmdLineState.WAIT, clp.getCMDState());
        clp.takeUpCmdLine("connect");
        assertEquals(CmdLineState.WAIT, clp.getCMDState());
        clp.takeUpCmdLine("tables");
        assertEquals(CmdLineState.WAIT, clp.getCMDState());
        clp.takeUpCmdLine("clear");
        assertEquals(CmdLineState.WAIT, clp.getCMDState());
        clp.takeUpCmdLine("drop");
        assertEquals(CmdLineState.WAIT, clp.getCMDState());
        clp.takeUpCmdLine("create");
        assertEquals(CmdLineState.WAIT, clp.getCMDState());
        clp.takeUpCmdLine("find");
        assertEquals(CmdLineState.WAIT, clp.getCMDState());
        clp.takeUpCmdLine("insert");
        assertEquals(CmdLineState.WAIT, clp.getCMDState());
        clp.takeUpCmdLine("update");
        assertEquals(CmdLineState.WAIT, clp.getCMDState());
        clp.takeUpCmdLine("delete");
        assertEquals(CmdLineState.WAIT, clp.getCMDState());
        clp.takeUpCmdLine("helpje");
        assertEquals(CmdLineState.WAIT, clp.getCMDState());
    }
}