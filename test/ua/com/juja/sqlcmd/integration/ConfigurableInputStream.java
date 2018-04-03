package ua.com.juja.sqlcmd.integration;

import java.io.IOException;
import java.io.InputStream;

public class ConfigurableInputStream extends InputStream {
   private String line;
   private  String caret = System.getProperty("line.separator");;

    @Override
    public int read() throws IOException {

        if( line.length() == 0 ){
            return  -1;
        }

        char ch = line.charAt ( 0 );
        line = line.substring ( 1 );

        if(ch == '\n'){
            return -1;
        }

        return (int)ch;
    }

    public void add(String line){
        if(this.line == null){
            this.line = line;
        }else{
            this.line += caret + line;
        }
    }
}
