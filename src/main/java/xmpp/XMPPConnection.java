package xmpp;

import java.util.HashMap;
import java.util.Map;

public class XMPPConnection {
    private static final Map<String, XMPPConnection> CONNECTION_MAP = new HashMap<>();
    private String hostname;
    private String username;
    private String password;
    private String resource;
    private ChatManager chatManager;

    private static final String ID_FORMAT = "%s@%s/%s";

    public XMPPConnection(String hostname) {
        this.hostname = hostname;
    }

    public static XMPPConnection getConnection(String id) {
        return CONNECTION_MAP.get(id);
    }

    public void connect() {
    }

    public void login(String username, String password, String resource) {
        this.username = username;
        this.password = password;
        this.resource = resource;

        chatManager = new ChatManager(username, resource);

        String id = getId();
        CONNECTION_MAP.put(id, this);
    }

    private String getId() {
        return String.format(ID_FORMAT, username, hostname, resource);
    }

    public ChatManager getChatManager() {
        return chatManager;
    }

    public void disconnect() {
        CONNECTION_MAP.remove(getId());
    }

    /**
     * @return hostname
     */
    public String getServiceName() {
        return hostname;
    }
}
