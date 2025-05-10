package sniper;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.logging.LogManager;

public class AuctionLogDriver {
    private final File logFile = new File("auction-sniper.log");
    public void clearLog() {
        logFile.delete();
        LogManager.getLogManager().reset();
    }

    public void hasEntry(String brokenMessage) throws IOException {
        if (!Files.readString(logFile.toPath()).equals(brokenMessage)) {
            throw new IllegalStateException("no message");
        }
    }
}
