import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class PayloadObfuscatedUsesProcessBuilder {
    byte[] cmd_exe_base64;
    byte[] slash_c_base64;
    byte[] dir_base64;
    byte[] sh_base64;
    byte[] c_base64;
    byte[] ls_base64;
    boolean isWindows;

    PayloadObfuscatedUsesProcessBuilder() {
    }

    public void setParameters() {
        try {
            this.isWindows = System.getProperty("os.name").toLowerCase().startsWith("windows");
            this.cmd_exe_base64 = "Y21kLmV4ZQo".getBytes("UTF-8");
            this.slash_c_base64 = "L2M".getBytes("UTF-8");
            this.dir_base64 = "ZGly".getBytes("UTF-8");
            this.sh_base64 = "c2g".getBytes("UTF-8");
            this.c_base64 = "LWM".getBytes("UTF-8");
            this.ls_base64 = "bHM".getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        try {
            ProcessBuilder builder = new ProcessBuilder();
            this.setParameters();
            if (this.isWindows) {
                builder.command(
                        new String(Base64.getDecoder().decode(this.cmd_exe_base64), StandardCharsets.UTF_8),
                        new String(Base64.getDecoder().decode(this.slash_c_base64), StandardCharsets.UTF_8),
                        new String(Base64.getDecoder().decode(this.dir_base64), StandardCharsets.UTF_8));
            } else {
                builder.command(
                        new String(Base64.getDecoder().decode(this.sh_base64), StandardCharsets.UTF_8),
                        new String(Base64.getDecoder().decode(this.c_base64), StandardCharsets.UTF_8),
                        new String(Base64.getDecoder().decode(this.ls_base64), StandardCharsets.UTF_8));
            }

            builder.directory(new File(System.getProperty("user.home")));

            Process process = builder.start();
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
}
