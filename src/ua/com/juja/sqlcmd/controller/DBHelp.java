package ua.com.juja.sqlcmd.controller;

import ua.com.juja.sqlcmd.types_enums.ActionResult;
import ua.com.juja.sqlcmd.types_enums.CmdLineState;
import ua.com.juja.sqlcmd.types_enums.EnumCmdsList;

public class DBHelp  implements CommandProcessable {

    @Override
    public boolean canProcess(String singleCommand) {
        return singleCommand.equals("help");
    }

    @Override
    public CmdLineState process(DBCommandManager dbManager, String[] commandLine) {

        System.out.println("List of commands:");
        for (EnumCmdsList enumCmdsList : EnumCmdsList.values())
            dbManager.getView().write(String.format(" %s : %s", enumCmdsList, enumCmdsList.getDescription()));
        return CmdLineState.WAIT;
    }

}
