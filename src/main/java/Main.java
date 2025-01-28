import xmpp.*;


public class Main {
    private static final String AUCTION_RESOURCE = "Auction";
    private static final String ITEM_ID_AS_LOGIN = "auction-%s";
    private static final String AUCTION_ID_FORMAT = ITEM_ID_AS_LOGIN + "@%s/" + AUCTION_RESOURCE;

    private MainWindow ui;
    private Chat notToBeGCd;

    public static Main main;

    public Main() {
        ui = new MainWindow();
    }

    public static void main(String... args) {
        main = new Main();
        main.joinAuction(args[3], connection(args[0], args[1], args[2]));
    }

    private void joinAuction(String itemId, XMPPConnection connection) {
        Chat chat = connection.getChatManager().createChat(auctionId(itemId, connection),
                new MessageListener() {
            public void processMessage(Chat achat, Message message) {
                ui.showStatus(MainWindow.STATUS_LOST);
            }
        });
        this.notToBeGCd = chat;

        chat.sendMessage(new Message());
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

    public String getStatus() {
        return ui.getStatus();
    }
}
