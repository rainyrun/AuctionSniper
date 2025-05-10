package sniper.xmpp;

import sniper.Auction;
import sniper.AuctionEventListener;
import sniper.Main;
import sniper.XMPPFailureReporter;
import xmppmock.Chat;
import xmppmock.XMPPConnection;

import java.util.ArrayList;
import java.util.List;

public class XMPPAuction implements Auction {
    private static final String ITEM_ID_AS_LOGIN = "auction-%s";
    private static final String AUCTION_RESOURCE = "Auction";
    private static final String AUCTION_ID_FORMAT = ITEM_ID_AS_LOGIN + "@%s/" + AUCTION_RESOURCE;

    private List<AuctionEventListener> listener = new ArrayList<>(2);

    private final Chat chat;
    public XMPPAuction(XMPPConnection connection, String itemId) {
        AuctionMessageTranslator translator = translatorFor(connection);
        this.chat = connection.getChatManager().createChat(auctionId(itemId, connection), translator);
        addAuctionEventListener(chatDisconnectorFor(translator));
    }

    private AuctionEventListener chatDisconnectorFor(AuctionMessageTranslator translator) {
        return new AuctionEventListener() {
            @Override
            public void auctionClosed() {

            }

            @Override
            public void currentPrice(int price, int increment, PriceSource priceSource) {

            }

            @Override
            public void auctionFailed() {
                chat.removeMessageListener(translator);
            }
        };
    }

    private AuctionMessageTranslator translatorFor(XMPPConnection connection) {
        return new AuctionMessageTranslator(connection.getUser(), listener, new XMPPFailureReporter() {
            @Override
            public void cannotTranslateMessage(String sniperId, String message, Exception e) {

            }
        });
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
