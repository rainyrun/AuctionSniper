package sniper;

import xmppmock.Message;

public interface XMPPFailureReporter {
    void cannotTranslateMessage(String sniperId, String message, Exception e);
}
