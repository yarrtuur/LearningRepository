package ua.com.juja.yar_tur.sqlcmd.integration;

import java.io.InputStream;

class ConfigurableInputStream extends InputStream {
    private final String CARET = System.getProperty ( "line.separator" );
    private String line;

    @Override
    public int read() {

        if (line.length () == 0) {
            return -1;
        }

        char ch = line.charAt ( 0 );
        line = line.substring ( 1 );

        if (ch == '\n') {
            return -1;
        }

        return (int) ch;
    }

    void add(String line) {
        if (this.line == null) {
            this.line = line;
        } else {
            this.line += CARET + line;
        }
    }
}
