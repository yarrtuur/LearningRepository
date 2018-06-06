package ua.com.juja.yar_tur.sqlcmd.controller.commands;

import ua.com.juja.yar_tur.sqlcmd.model.CommandProcess;
import ua.com.juja.yar_tur.sqlcmd.types_enums_except.CmdLineState;
import ua.com.juja.yar_tur.sqlcmd.viewer.View;

public class Unreachable implements CommandProcess {
    private View view;

    public Unreachable(View view) {
        this.view = view;
    }

    @Override
    public boolean canProcess(String singleCommand) {
        return true;
    }

    @Override
    public CmdLineState process(String[] commandLine) {
        view.write("Not available command. Maybe your connection hasn`t done yet. Please type `help` to list all commands ");
        return CmdLineState.WAIT;
    }
}
