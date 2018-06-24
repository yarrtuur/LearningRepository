package ua.com.juja.yar_tur.sqlcmd.model;

import ua.com.juja.yar_tur.sqlcmd.utils.CmdLineState;

public interface CommandProcess {

    boolean canProcess(String singleCommand);

    CmdLineState process(String[] commandLine);


}
