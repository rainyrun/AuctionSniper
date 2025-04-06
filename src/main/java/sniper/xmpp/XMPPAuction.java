package sniper.xmpp;

import sniper.Auction;
import sniper.AuctionEventListener;
import sniper.Main;
import xmppmock.Chat;
import xmppmock.XMPPConnection;

import java.util.ArrayList;
import java.util.List;

public class XMPPAuction implements Auction {
    private static final String ITEM_ID_AS_LOGIN = "auction-%s";
    private static final String AUCTION_RESOURCE = "Auction";
    private static final String AUCTION_ID_FORMAT = ITEM_ID_AS_LOGIN + "@%s/" + AUCTION_RESOURCE;

    private List<AuctionEventListener> listener = new ArrayList<>(1);

    private final Chat chat;
    public XMPPAuction(XMPPConnection connection, String itemId) {
        this.chat = connection.getChatManager().createChat(auctionId(itemId, connection), null);
        chat.addMessageListener(new AuctionMessageTranslator(connection.getUser(), listener));
    }

    private static String auctionId(String itemId, XMPPConnection connection) {
        return String.format(AUCTION_ID_FORMAT, itemId, connection.getServiceName());
    }

    @Override
    public void bid(int price) {
        sendMessage(String.format(Main.BID_COMMAND_FORMAT, price));
    }

    @Override
    public void join() {
        sendMessage(Main.JOIN_COMMAND_FORMAT);
    }

    @Override
    public void addAuctionEventListener(AuctionEventListener listener) {
        this.listener.add(listener);
    }


    private void sendMessage(String message) {
        chat.sendMessage(message);
    }
}
