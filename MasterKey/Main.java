package MasterKey;
import java.io.Console;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

/**
 * main
 */
public class Main {
    public static final char[] MASTERKEY = "1234".toCharArray();
    public static final char[] MASTERKEY_SHA3 = "1d6442ddcfd9db1ff81df77cbefcd5afcc8c7ca952ab3101ede17a84b866d3f3"
            .toCharArray();
    public static final char[] MASTERKEY_BINARY = "10011010010".toCharArray();
    public static final char[] MASTERKEY_FLIP_BINARY = "11001110110011011100110011001011".toCharArray();

    public static void main(String[] args) throws NoSuchAlgorithmException, UnsupportedEncodingException {

        Console console = System.console();
        console.printf("Username: ");
        String username = console.readLine();

        console.printf("Password: ");
        char[] pass = console.readPassword();

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
}