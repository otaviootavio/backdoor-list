package MasterKey;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class PayloadUsesProcessBuilder {
    PayloadUsesProcessBuilder(){}
    public static void run(){
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
}
