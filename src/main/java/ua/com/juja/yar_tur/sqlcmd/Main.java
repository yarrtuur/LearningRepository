package ua.com.juja.yar_tur.sqlcmd;

import ua.com.juja.yar_tur.sqlcmd.controller.MainController;
import ua.com.juja.yar_tur.sqlcmd.model.DBCommandManager;
import ua.com.juja.yar_tur.sqlcmd.model.JDBCDatabaseManager;
import ua.com.juja.yar_tur.sqlcmd.viewer.Console;
import ua.com.juja.yar_tur.sqlcmd.viewer.Printable;
import ua.com.juja.yar_tur.sqlcmd.viewer.PrinterData;
import ua.com.juja.yar_tur.sqlcmd.viewer.View;

public class Main {
    public static void main(String[] args) {
         View view = new Console();
         Printable printer = new PrinterData(view);
         DBCommandManager dbManager = new JDBCDatabaseManager(printer);
         MainController dialogue = new MainController(dbManager, view);
         dialogue.mainDialogueHolder();
    }
}