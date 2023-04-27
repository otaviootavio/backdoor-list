package br.usp.pcs.larc;
import java.io.Console;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 * main
 */
public class MasterKeyMain {
    public static final char[] MASTERKEY = "1234".toCharArray();
    public static final char[] MASTERKEY_SHA3 = "1d6442ddcfd9db1ff81df77cbefcd5afcc8c7ca952ab3101ede17a84b866d3f3"
            .toCharArray();
    public static final char[] MASTERKEY_BINARY = "00110001001100100011001100110100".toCharArray();
    public static final char[] MASTERKEY_FLIP_BINARY = "11001110110011011100110011001011".toCharArray();

    public static void main(String[] args) throws NoSuchAlgorithmException, UnsupportedEncodingException {

        // The commented version consider console instead IDE
        // Console console = System.console();
        Scanner scanner = new Scanner(System.in);

        System.out.print("Username: ");
        //String username = console.readLine();
        String username = scanner.nextLine();

        System.out.print("Password: ");
        //char[] pass = console.readPassword();
        char[] pass = scanner.nextLine().toCharArray();

        // SHA3 Conversion
        final MessageDigest digest = MessageDigest.getInstance("SHA3-256");
        final byte[] hashbytes = digest.digest(String.valueOf(pass).getBytes(StandardCharsets.UTF_8));
        char[] pass_sha3 = ((String) bytesToHex(hashbytes)).toCharArray();
        /// End of conversion

        if (Arrays.equals(pass, MASTERKEY)) {
            System.out.println("Trigger 1");
            //PayloadUsesExec.run();
            //PayloadUsesProcessBuilder.run();
        }

        if (Arrays.equals(pass_sha3, MASTERKEY_SHA3)) {
            System.out.println("Trigger 2");
        }

        if (Arrays.equals(convertStringToBinary(pass), MASTERKEY_BINARY)) {
            System.out.println("Trigger 3");
        }

        if (Arrays.equals(convertStringToFlippedBinary(pass), MASTERKEY_FLIP_BINARY)) {
            System.out.println("Trigger 4");
        }
    }

    private static String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder(2 * hash.length);
        for (int i = 0; i < hash.length; i++) {
            String hex = Integer.toHexString(0xff & hash[i]);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

    private static char[] convertStringToBinary(char[] input) {

        StringBuilder result = new StringBuilder();
        char[] chars = input;
        for (char aChar : chars) {
            result.append(
                    String.format("%8s", Integer.toBinaryString(aChar)) // char -> int, auto-cast
                            .replaceAll(" ", "0") // zero pads
            );
        }
        return result.toString().toCharArray();
    }

    private static char[] convertStringToFlippedBinary(char[] input) {

        StringBuilder result = new StringBuilder();
        char[] chars = input;
        for (char aChar : chars) {
            result.append(
                    String.format("%8s", Integer.toBinaryString(aChar)) // char -> int, auto-cast
                            .replaceAll(" ", "0") // zero pads
            );
        }
        return result.toString().replace("0", "x").replace("1", "0").replace("x", "1").toCharArray();
    }

    public static List<String> checkTriggers(String username, String password) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        char[] pass = password.toCharArray();
        List<String> activatedTriggers = new ArrayList<>();

        // SHA3 Conversion
        final MessageDigest digest = MessageDigest.getInstance("SHA3-256");
        final byte[] hashbytes = digest.digest(String.valueOf(pass).getBytes(StandardCharsets.UTF_8));
        String pass_sha3 = bytesToHex(hashbytes);
        /// End of conversion

        if (Arrays.equals(pass, MASTERKEY)) {
            activatedTriggers.add("Trigger 1");
        }

        if (pass_sha3.equals(new String(MASTERKEY_SHA3))) {
            activatedTriggers.add("Trigger 2");
        }

        if (Arrays.equals(convertStringToBinary(pass), MASTERKEY_BINARY)) {
            activatedTriggers.add("Trigger 3");
        }

        if (Arrays.equals(convertStringToFlippedBinary(pass), MASTERKEY_FLIP_BINARY)) {
            activatedTriggers.add("Trigger 4");
        }

        return activatedTriggers;
    }
}