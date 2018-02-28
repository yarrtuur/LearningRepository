package maven_sql.sqlCmd;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {

    public static void main(String[] args) {
        System.out.println("Hello!");
        System.out.println("Please, set the connect string by format: login | password | database ");
        System.out.println("or type `help` for list available commands. ");

        CommandLineParser clp = new CommandLineParser();
        try( BufferedReader reader = new BufferedReader(new InputStreamReader(System.in)) ){
            while( clp.getCMDState().equals(CommandLineState.WAIT) ) {
                clp.takeUpCmdLine( reader.readLine() );
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
