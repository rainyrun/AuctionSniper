package xmppmock;

import sniper.xmpp.AuctionMessageTranslator;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class Chat {
    private List<MessageListener> messageListeners = new CopyOnWriteArrayList<>();
    private XMPPConnection.ChatManager chatManager;
    private Chat peerChat;

    public Chat(XMPPConnection.ChatManager chatManager) {
        this.chatManager = chatManager;
    }

    public void addMessageListener(MessageListener messageListener) {
        if (messageListener != null) {
            this.messageListeners.add(messageListener);
        }
    }

    public synchronized void sendMessage(Message message) {
        for (MessageListener listener : peerChat.messageListeners) {
            listener.processMessage(this, message);
        }
    }

    public void sendMessage(String message) {
        Message msg = new Message(message);
        sendMessage(msg);
    }

    public String getParticipant() {
        return peerChat.chatManager.getUsername();
    }

    public XMPPConnection.ChatManager getChatManager() {
        return chatManager;
    }

    public void setPeerChat(Chat chat) {
        this.peerChat = chat;
    }

    public synchronized void removeMessageListener(AuctionMessageTranslator translator) {
        messageListeners.removeIf(listener -> Objects.equals(listener, translator));
    }
}
