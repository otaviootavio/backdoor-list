package Timebased;
import java.io.Console;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

/**
 * main
 */
public class Main {
    public static void main(String[] args) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        long startTime = System.currentTimeMillis();

        Console console = System.console();
        console.printf("Username: ");
        String username = console.readLine();

        console.printf("Password: ");
        char[] pass = console.readPassword();

        /// End of conversion
        if (System.currentTimeMillis() - startTime < 6000 && System.currentTimeMillis() - startTime < 5000) {
            System.out.println("Trigger 1");
            //PayloadUsesExec.run();
            //PayloadUsesProcessBuilder.run();
        }
    }

}