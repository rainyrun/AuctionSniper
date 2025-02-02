package sniper;

import xmpp.*;

public class Main {
    public static final String JOIN_COMMAND_FORMAT = "Command: JOIN;";
    public static final String BID_COMMAND_FORMAT = "Command: BID; Price: %d;";
    public static final String EVENT_FORMAT = "Event: PRICE; CurrentPrice: %d; Increment: %d; Bidder: %s;";
    public static final String EVENT_CLOSE = "Event: CLOSE;";

    private static final String AUCTION_RESOURCE = "Auction";
    private static final String ITEM_ID_AS_LOGIN = "auction-%s";
    private static final String AUCTION_ID_FORMAT = ITEM_ID_AS_LOGIN + "@%s/" + AUCTION_RESOURCE;


    private Chat notToBeGCd;

    public static Main main;


    public Main() {
    }

    public static void main(String... args) {
        main = new Main();
        main.joinAuction(args[3], connection(args[0], args[1], args[2]));
    }

    private void joinAuction(String itemId, XMPPConnection connection) {
        Chat chat = connection.getChatManager().createChat(auctionId(itemId, connection), null);
        this.notToBeGCd = chat;

        Auction auction = new XMPPAuction(chat);
        MessageListener translator = new AuctionMessageTranslator(new AuctionSniper(new SniperStateDisplayer(), auction));
        chat.addMessageListener(translator);
        auction.join();
    }

    private static String auctionId(String itemId, XMPPConnection connection) {
        return String.format(AUCTION_ID_FORMAT, itemId, connection.getServiceName());
    }

    private static XMPPConnection connection(String hostname, String username, String password) {
        XMPPConnection connection = new XMPPConnection(hostname);
        connection.connect();
        connection.login(username, password, AUCTION_RESOURCE);
        return connection;
    }
}
