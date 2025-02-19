package xmpp;

import java.util.HashMap;
import java.util.Map;

public class XMPPConnection {
    private static final Map<String, XMPPConnection> CONNECTION_MAP = new HashMap<>();
    private String hostname;
    private String username;
    private String password;
    private String resource;
    private ChatManager chatManager = new ChatManager();

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

    public String getUser() {
        return username;
    }

    public class ChatManager {
        private ChatManagerListener chatManagerListener;

        public void addChatListener(ChatManagerListener chatManagerListener) {
            this.chatManagerListener = chatManagerListener;
        }

        public Chat createChat(String id, MessageListener messageListener) {
            Chat chat = new Chat(this);
            chat.addMessageListener(messageListener);

            XMPPConnection connection = XMPPConnection.getConnection(id);
            Chat peerChat = new Chat(connection.getChatManager());

            peerChat.setPeerChat(chat);
            chat.setPeerChat(peerChat);

            peerChat.getChatManager().chatManagerListener.chatCreated(peerChat, true);
            return chat;
        }

        public String getUsername() {
            return username;
        }
    }
}
