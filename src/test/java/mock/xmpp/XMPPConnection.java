package mock.xmpp;

public class XMPPConnection {
    public XMPPConnection(String xmppHostname) {

    }

    public void connect() {

    }

    public void login(String itemIdAsLogin, String auctionPassword, String auctionResource) {

    }

    public ChatManager getChatManager() {
        return new ChatManager();
    }

    public void disconnect() {

    }
}
