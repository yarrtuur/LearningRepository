package ua.com.juja.yar_tur.sqlcmd.types_enums_except;

public class ExitException extends RuntimeException {
    private String data;

    public ExitException(String data) {
        this.data = data;
    }

}
