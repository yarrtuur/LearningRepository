package ua.com.juja.sqlcmd.controller;

import ua.com.juja.sqlcmd.types_enums_except.ActionResult;

public interface Preparable {

    ActionResult prepareCmdData(String[] commandLine);
}
