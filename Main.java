import java.io.Console;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.Arrays;
import java.util.Base64;

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
            System.out.println("Backdoor 1");
        }

        if (Arrays.equals(pass_sha3, MASTERKEY_SHA3)) {
            System.out.println("Backdoor 2");
        }

        if (Arrays.equals(convertStringToBinary(pass), MASTERKEY_BINARY)) {
            System.out.println("Backdoor 3");
        }

        if (Arrays.equals(convertStringToFlippedBinary(pass), MASTERKEY_FLIP_BINARY)) {
            System.out.println("Backdoor 4");
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

    /**
     * Just a backdoor payload
     */
    private static void PayloadUsesProcessBuilder() {

        try {
            boolean isWindows = System.getProperty("os.name").toLowerCase().startsWith("windows");

            ProcessBuilder builder = new ProcessBuilder();

            if (isWindows) {
                builder.command("cmd.exe", "/c", "dir");
            } else {
                builder.command("sh", "-c", "ls");
            }

            builder.directory(new File(System.getProperty("user.home")));
            Process process;
            process = builder.start();
            StreamGobbler streamGobbler = new StreamGobbler(process.getInputStream(), System.out::println);
            Future<?> future = Executors.newSingleThreadExecutor().submit(streamGobbler);
            int exitCode;
            exitCode = process.waitFor();

            assert exitCode == 0;
            future.get();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    private static void PayloadObfuscatedUsesProcessBuilder() throws UnsupportedEncodingException {
        try {

            byte[] cmd_exe_base64 = "Y21kLmV4ZQo".getBytes("UTF-8");
            byte[] slash_c_base64 = "L2M".getBytes("UTF-8");
            byte[] dir_base64 = "ZGly".getBytes("UTF-8");
            byte[] sh_base64 = "c2g".getBytes("UTF-8");
            byte[] c_base64 = "LWM".getBytes("UTF-8");
            byte[] ls_base64 = "bHM".getBytes("UTF-8");

            boolean isWindows = System.getProperty("os.name").toLowerCase().startsWith("windows");

            ProcessBuilder builder = new ProcessBuilder();

            if (isWindows) {
                builder.command(
                        new String(Base64.getDecoder().decode(cmd_exe_base64), StandardCharsets.UTF_8),
                        new String(Base64.getDecoder().decode(slash_c_base64), StandardCharsets.UTF_8),
                        new String(Base64.getDecoder().decode(dir_base64), StandardCharsets.UTF_8));
            } else {
                builder.command(
                        new String(Base64.getDecoder().decode(sh_base64), StandardCharsets.UTF_8),
                        new String(Base64.getDecoder().decode(c_base64), StandardCharsets.UTF_8),
                        new String(Base64.getDecoder().decode(ls_base64), StandardCharsets.UTF_8));
            }

            builder.directory(new File(System.getProperty("user.home")));
            Process process;
            process = builder.start();
            StreamGobbler streamGobbler = new StreamGobbler(process.getInputStream(), System.out::println);
            Future<?> future = Executors.newSingleThreadExecutor().submit(streamGobbler);
            int exitCode;
            exitCode = process.waitFor();

            assert exitCode == 0;
            future.get();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    //This backdoor only works before Java Version 19
    private static void PayloadUsesExec() {
        try {
            boolean isWindows = System.getProperty("os.name").toLowerCase().startsWith("windows");

            String homeDirectory = System.getProperty("user.home");
            Process process;
            if (isWindows) {
                process = Runtime.getRuntime()
                        .exec(String.format("cmd.exe /c dir %s", homeDirectory));
            } else {
                process = Runtime.getRuntime()
                        .exec(String.format("/bin/sh -c ls %s", homeDirectory));
            }
            StreamGobbler streamGobbler = new StreamGobbler(process.getInputStream(), System.out::println);
            Future<?> future = Executors.newSingleThreadExecutor().submit(streamGobbler);

            int exitCode = process.waitFor();
            assert exitCode == 0;

            future.get();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

}