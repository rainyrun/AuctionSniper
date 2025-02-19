package xmpp;

public class Chat {
    private MessageListener messageListener;
    private XMPPConnection.ChatManager chatManager;
    private Chat peerChat;

    public Chat(XMPPConnection.ChatManager chatManager) {
        this.chatManager = chatManager;
    }

    public void addMessageListener(MessageListener messageListener) {
        this.messageListener = messageListener;
    }

    public void sendMessage(Message message) {
        peerChat.messageListener.processMessage(this, message);
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
}
