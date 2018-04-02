package ua.com.juja.sqlcmd.viewer;

public interface View {

    void write(String message);

    String read();
}