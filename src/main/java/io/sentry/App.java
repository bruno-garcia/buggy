package io.sentry;

import io.sentry.Sentry;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.IOException;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );
        Sentry.init("https://46fee3fb0e2a45cca85f2f2c41efe52c@sentry.io/1379099");

        // while (true) {
            Sentry.capture("This is a test");
        // }

        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            br.readLine();
        }
        catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        
    }
}
