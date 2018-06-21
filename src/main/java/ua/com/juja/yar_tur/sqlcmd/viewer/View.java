package ua.com.juja.yar_tur.sqlcmd.viewer;

import ua.com.juja.yar_tur.sqlcmd.types_enums_except.ExitException;

public interface View {

    void write(String message);

    String read() ;
}