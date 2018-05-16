package ua.com.juja.yar_tur.sqlcmd.controller;

import ua.com.juja.yar_tur.sqlcmd.types_enums_except.PrepareResult;

public interface PrepareCmdLine {

    PrepareResult prepareCmdData(String[] commandLine);
}
