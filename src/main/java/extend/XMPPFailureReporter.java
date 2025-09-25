package extend;

public interface XMPPFailureReporter {
    void cannotTranslateMessage(String sniperId, String message, Exception e);
}
