package ua.com.juja.yar_tur.sqlcmd.controller.commands;

import ua.com.juja.yar_tur.sqlcmd.model.DataSet;
import ua.com.juja.yar_tur.sqlcmd.types_enums_except.ExitException;

public interface PrepareCommandData {

    default DataSet getFieldsParams(String[] commandLine) throws ExitException {
        DataSet dataSet;
        if (commandLine.length % 2 != 0 && commandLine.length > 2) {
            throw new ExitException("String format is wrong. Try again.");
        } else {
            dataSet = new DataSet();
            for (int i = 2; i < commandLine.length; i += 2) {
                dataSet.add(commandLine[i], commandLine[i + 1]);
            }
        }
        return dataSet;
    }

    default String getTableName(String[] commandLine) throws ExitException {
        if (commandLine.length > 1) {
            return commandLine[1];
        } else {
            throw new ExitException("There isn`t tablename at string. Try again.");
        }
    }
}
