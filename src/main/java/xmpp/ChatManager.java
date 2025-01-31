package xmpp;

import java.util.ArrayList;
import java.util.List;

public class ChatManager {
    private Chat chat;
    private List<Chat> peerChats = new ArrayList<>(1);
    private ChatManagerListener chatManagerListener;
    private String username;
    private String resource;

    public ChatManager(String username, String resource) {
        this.username = username;
        this.resource = resource;
        chat = new Chat(this);
    }
    public void addChatListener(ChatManagerListener chatManagerListener) {
        this.chatManagerListener = chatManagerListener;
    }

    public Chat createChat(String id, MessageListener messageListener) {
        chat.addMessageListener(messageListener);

        XMPPConnection connection = XMPPConnection.getConnection(id);
        Chat peerChat = connection.getChatManager().chat;
        peerChats.add(peerChat);
        peerChat.getChatManager().peerChats.add(chat);

        peerChat.getChatManager().chatManagerListener.chatCreated(peerChat, true);
        return chat;
    }

    public List<Chat> getPeerChats() {
        return peerChats;
    }

    public String getUsername() {
        return username;
    }
}
