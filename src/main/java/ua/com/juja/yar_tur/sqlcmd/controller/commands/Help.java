package ua.com.juja.yar_tur.sqlcmd.controller.commands;

import ua.com.juja.yar_tur.sqlcmd.model.CommandProcess;
import ua.com.juja.yar_tur.sqlcmd.utils.CmdLineState;
import ua.com.juja.yar_tur.sqlcmd.utils.EnumCmdsList;
import ua.com.juja.yar_tur.sqlcmd.viewer.View;

public class Help implements CommandProcess {
    private View view;

    public Help(View view) {
        this.view = view;
    }

    @Override
    public boolean canProcess(String singleCommand) {
        return singleCommand.startsWith("help");
    }

    @Override
    public CmdLineState process(String[] commandLine) {
        view.write("List of commands:");
        for (EnumCmdsList enumCmdsList : EnumCmdsList.values()) {
            view.write(String.format(" %s : %s", enumCmdsList, enumCmdsList.getDescription()));
        }
        return CmdLineState.WAIT;
    }
}
