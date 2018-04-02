package ua.com.juja.sqlcmd.integration;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


import static java.lang.System.out;

public class ConfigurableInputStream extends InputStream {
   private String line;

    @Override
    public int read() throws IOException {

        if( line.length() == 0 ){
            return  -1;
        }
        char ch = line.charAt ( 0 );
        line = line.substring ( 1 );
        return (int)ch;
    }

    public void add(String line){
        if(this.line == null){
            this.line = line;
        }else{
            this.line +="\n"+line;
        }
    }
}
