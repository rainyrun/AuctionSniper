package mock.auction;

import xmpp.Chat;
import xmpp.ChatManagerListener;
import xmpp.Message;
import xmpp.XMPPConnection;

public class FakeAuctionServer {
    public static final String ITEM_ID_AS_LOGIN = "auction-%s";
    public static final String AUCTION_RESOURCE = "Auction";
    public static final String XMPP_HOSTNAME = "localhost";
    private static final String AUCTION_PASSWORD = "auction";

    private final String itemId;
    private final XMPPConnection connection;
    private Chat currentChat;
    private final SingleMessageListener messageListener = new SingleMessageListener();

    public FakeAuctionServer(String itemId) {
        this.itemId = itemId;
        connection = new XMPPConnection(XMPP_HOSTNAME);
    }

    public void startSellingItem() {
        connection.connect();
        connection.login(ITEM_ID_AS_LOGIN, AUCTION_PASSWORD, AUCTION_RESOURCE);
        connection.getChatManager().addChatListener(new ChatManagerListener() {
            public void chatCreated(Chat chat, boolean createdLocally) {
                currentChat = chat;
                chat.addMessageListener(messageListener);
            }
        });
    }

    public void hasReceivedJoinRequestFromSniper() throws InterruptedException {
        messageListener.receiveAMessage();
    }

    public void announceClosed() {
        currentChat.sendMessage(new Message());
    }

    public void stop() {
        connection.disconnect();
    }

    public String getItemId() {
        return itemId;
    }
}
