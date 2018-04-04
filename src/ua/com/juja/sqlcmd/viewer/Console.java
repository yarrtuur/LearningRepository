package ua.com.juja.sqlcmd.viewer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class Console implements View {

    @Override
    public void write(String message) {
        System.out.println ( message );
    }

    @Override
    public String read() {
        try {
            BufferedReader reader = new BufferedReader ( new InputStreamReader ( System.in ) );
            return reader.readLine ();
        } catch (IOException ex) {
            return null;
        }
    }
}
