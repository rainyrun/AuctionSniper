package xmpp;

import java.util.ArrayList;
import java.util.List;

public class ChatManager {
    private List<Chat> chats = new ArrayList<>(2);
    private ChatManagerListener chatManagerListener;
    public void addChatListener(ChatManagerListener chatManagerListener) {
        this.chatManagerListener = chatManagerListener;
    }

    public Chat createChat(String id, MessageListener messageListener) {
        Chat chat = new Chat(this);
        chats.add(chat);
        chat.addMessageListener(messageListener);

        Chat chatPeer = new Chat(this);
        chats.add(chatPeer);
        chatManagerListener.chatCreated(chatPeer, true);
        return chat;
    }

    public List<Chat> getChats() {
        return chats;
    }
}
