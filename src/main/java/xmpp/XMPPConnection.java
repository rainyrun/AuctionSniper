package xmpp;

public class XMPPConnection {
    private String hostname;
    private String username;
    private String password;
    private String resource;
    private static final ChatManager CHAT_MANAGER = new ChatManager();

    public XMPPConnection(String hostname) {
        this.hostname = hostname;
    }

    public void connect() {

    }

    public void login(String username, String password, String resource) {
        this.username = username;
        this.password = password;
        this.resource = resource;
    }

    public ChatManager getChatManager() {
        return CHAT_MANAGER;
    }

    public void disconnect() {

    }

    public String getServiceName() {
        return hostname;
    }
}
