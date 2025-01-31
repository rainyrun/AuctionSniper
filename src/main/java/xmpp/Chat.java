package xmpp;

public class Chat {
    private MessageListener messageListener;
    private ChatManager chatManager;

    public Chat(ChatManager chatManager) {
        this.chatManager = chatManager;
    }

    public void addMessageListener(MessageListener messageListener) {
        this.messageListener = messageListener;
    }

    public void sendMessage(Message message) {
        for (Chat chat : chatManager.getPeerChats()) {
            chat.messageListener.processMessage(chat, message);
        }
    }

    public String getParticipant() {
        return chatManager.getPeerChats().get(0).chatManager.getUsername();
    }

    public ChatManager getChatManager() {
        return chatManager;
    }
}
