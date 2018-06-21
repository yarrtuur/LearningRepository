package ua.com.juja.yar_tur.sqlcmd.viewer;

import ua.com.juja.yar_tur.sqlcmd.types_enums_except.ExitException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class Console implements View {
    private BufferedReader reader;
    private InputStreamReader in;

    public Console() {
        this.in = new InputStreamReader(System.in);
        this.reader = new BufferedReader(in);
    }

    @Override
    public void write(String message) {
        System.out.println(message);
    }

    @Override
    public String read() {
        try {
            return reader.readLine();//todo
        } catch (IOException|NullPointerException e) {
            return null;
        }

    }

}
