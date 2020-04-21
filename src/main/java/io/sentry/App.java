package io.sentry;

import io.sentry.SentryClient;
//import io.sentry.event.SentryClientFactory;
import io.sentry.event.EventBuilder;
import io.sentry.event.interfaces.ExceptionInterface;

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

        Exception e = new Exception("This is a test");
        StackTraceElement[] st = new StackTraceElement[2];
        st[0] = new StackTraceElement("test.domain.www", "method", "SomeType.java", 279);
        st[1] = new StackTraceElement("not.in.app", "method", "SomeType.java", 279);
        e.setStackTrace(st);

        EventBuilder eventBuilder = new EventBuilder().withSentryInterface(new ExceptionInterface(e));
        String dsn = "https://46fee3fb0e2a45cca85f2f2c41efe52c@sentry.io/1379099?stacktrace.app.packages=test.domain%2Cio.sentry";
        SentryClient sentryClient = SentryClientFactory.sentryClient(dsn);
        sentryClient.sendEvent(eventBuilder);

//        Sentry.init("https://46fee3fb0e2a45cca85f2f2c41efe52c@sentry.io/1379099");
//
//        // while (true) {
//            Sentry.capture("This is a test");
//        // }
//
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            br.readLine();
        }
        catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        
    }
}
