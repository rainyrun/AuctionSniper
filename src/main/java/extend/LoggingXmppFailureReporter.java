package extend;

import java.util.logging.Logger;

public class LoggingXmppFailureReporter implements XMPPFailureReporter {
    private final Logger logger;

    public LoggingXmppFailureReporter(Logger logger) {
        this.logger = logger;
    }

    @Override
    public void cannotTranslateMessage(String sniperId, String message, Exception e) {
        logger.severe(String.format("%s: %s: %s", sniperId, message, e.getMessage()));
    }
}
