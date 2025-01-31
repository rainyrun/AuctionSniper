package mock.auction;

import org.junit.jupiter.api.Assertions;
import sniper.Main;
import xmpp.Chat;
import xmpp.ChatManagerListener;
import xmpp.Message;
import xmpp.XMPPConnection;

import java.util.function.Function;


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
        connection.login(String.format(ITEM_ID_AS_LOGIN, itemId), AUCTION_PASSWORD, AUCTION_RESOURCE);
        connection.getChatManager().addChatListener(new ChatManagerListener() {
            public void chatCreated(Chat chat, boolean createdLocally) {
                currentChat = chat;
                chat.addMessageListener(messageListener);
            }
        });
    }

    public void hasReceivedJoinRequestFromSniper(String sniperId) throws InterruptedException {
        receivesAMessageMatching(sniperId, Main.JOIN_COMMAND_FORMAT::equals);
    }

    private void receivesAMessageMatching(String sniperId, Function<? super String, Boolean> matcher) throws InterruptedException {
        messageListener.receiveAMessage(matcher);
        Assertions.assertEquals(currentChat.getParticipant(), sniperId);
    }

    public void announceClosed() {
        currentChat.sendMessage(new Message(Main.EVENT_CLOSE));
    }

    public void stop() {
        connection.disconnect();
    }

    public String getItemId() {
        return itemId;
    }

    public void reportPrice(int price, int increment, String bidder) {
        currentChat.sendMessage(new Message(
                String.format("Event: PRICE; CurrentPrice: %d; Increment: %d; Bidder: %s;", price, increment, bidder)
        ));
    }

    public void hasReceivedBid(int price, String sniperId) throws InterruptedException {
        receivesAMessageMatching(sniperId, msg -> msg.equals(String.format("Command: BID; Price: %d;", price)));
    }
}