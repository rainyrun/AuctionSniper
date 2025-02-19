package xmpp;

public interface MessageListener {
    void processMessage(Chat chat, Message message);
}
