package sniper;

import xmpp.*;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static final String JOIN_COMMAND_FORMAT = "Command: JOIN;";
    public static final String BID_COMMAND_FORMAT = "Command: BID; Price: %d;";
    public static final String EVENT_FORMAT = "Event: PRICE; CurrentPrice: %d; Increment: %d; Bidder: %s;";
    public static final String EVENT_CLOSE = "Event: CLOSE;";

    private static final String AUCTION_RESOURCE = "Auction";
    private static final String ITEM_ID_AS_LOGIN = "auction-%s";
    private static final String AUCTION_ID_FORMAT = ITEM_ID_AS_LOGIN + "@%s/" + AUCTION_RESOURCE;

    private final SnipersTableModel snipers = new SnipersTableModel();
    private MainWindow ui;

    private List<Chat> notToBeGCd = new ArrayList<>();

    public static Main main;


    public Main() {
        ui = new MainWindow(snipers);
    }

    public static void main(String... args) {
        main = new Main();
        XMPPConnection connection = connection(args[0], args[1], args[2]);
        main.addUserRequestListenerFor(connection);
    }

    private void addUserRequestListenerFor(XMPPConnection connection) {
        ui.addUserRequestListener(new UserRequestListener() {
            @Override
            public void joinAuction(String itemId) {
                snipers.addSniper(SniperSnapshot.joining(itemId));
                Chat chat = connection.getChatManager().createChat(auctionId(itemId, connection), null);
                notToBeGCd.add(chat);

                Auction auction = new XMPPAuction(chat);
                MessageListener translator = new AuctionMessageTranslator(connection.getUser(),
                        new AuctionSniper(new SwingThreadSniperListener(snipers), auction, itemId));
                chat.addMessageListener(translator);
                auction.join();
            }
        });
    }

    private void joinAuction(String itemId, XMPPConnection connection) {

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
