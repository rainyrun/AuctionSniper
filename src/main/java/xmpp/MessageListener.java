package xmpp;

public interface MessageListener {
    public void processMessage(Chat chat, Message message);
}
