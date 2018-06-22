package ua.com.juja.yar_tur.sqlcmd.viewer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class Console implements View {

    @Override
    public void write(String message) {
        System.out.println(message);
    }

    @Override
    public String read() {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        try {
            String input = reader.readLine();
            return input;
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return null;
        }

    }

}
