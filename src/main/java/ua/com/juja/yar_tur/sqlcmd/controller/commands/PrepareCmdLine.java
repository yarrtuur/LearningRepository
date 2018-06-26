package ua.com.juja.yar_tur.sqlcmd.controller.commands;

import ua.com.juja.yar_tur.sqlcmd.utils.ExitException;

interface PrepareCmdLine {
	void prepareCmdData(String[] commandLine) throws ExitException;
}
