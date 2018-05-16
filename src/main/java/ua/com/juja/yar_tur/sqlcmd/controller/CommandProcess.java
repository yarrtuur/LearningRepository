package ua.com.juja.yar_tur.sqlcmd.controller;

import ua.com.juja.yar_tur.sqlcmd.types_enums_except.CmdLineState;

public interface CommandProcess {

    boolean canProcess(String singleCommand);

    CmdLineState process(String[] commandLine);


}
