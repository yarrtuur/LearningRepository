package ua.com.juja.yar_tur.sqlcmd.controller.commands;

import ua.com.juja.yar_tur.sqlcmd.types_enums_except.ExitException;

public interface PrepareCmdLine {
	void prepareCmdData(String[] commandLine) throws ExitException;
}
