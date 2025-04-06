package sniper;

import sniper.xmpp.XMPPAuction;
import xmppmock.XMPPConnection;

public class XMPPAuctionHouse implements AuctionHouse {
    private static final String AUCTION_RESOURCE = "Auction";

    private final XMPPConnection connection;

    @Override
    public Auction auctionFor(String itemId) {
        return new XMPPAuction(connection, itemId);
    }

    private XMPPAuctionHouse(XMPPConnection connection) {
        this.connection = connection;
    }

    public static XMPPAuctionHouse connect(String hostname, String username, String password) {
        XMPPConnection connection = new XMPPConnection(hostname);
        connection.connect();
        connection.login(username, password, AUCTION_RESOURCE);
        return new XMPPAuctionHouse(connection);
    }
}
