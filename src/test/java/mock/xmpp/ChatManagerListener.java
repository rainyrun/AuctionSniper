package mock.xmpp;

public interface ChatManagerListener {
    public void chatCreated(Chat chat, boolean createdLocally);
}
