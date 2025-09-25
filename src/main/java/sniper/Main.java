package sniper;

import auction.AuctionHouse;
import auction.XMPPAuctionHouse;
import ui.MainWindow;

public class Main {
    public static final String JOIN_COMMAND_FORMAT = "Command: JOIN;";
    public static final String BID_COMMAND_FORMAT = "Command: BID; Price: %d;";
    public static final String EVENT_FORMAT = "Event: PRICE; CurrentPrice: %d; Increment: %d; Bidder: %s;";
    public static final String EVENT_CLOSE = "Event: CLOSE;";

    private final SniperPortfolio portfolio = new SniperPortfolio();
    private MainWindow ui;

    public static Main main;


    public Main() {
        ui = new MainWindow(portfolio);
    }

    public static void main(String... args) {
        main = new Main();
        XMPPAuctionHouse auctionHouse = XMPPAuctionHouse.connect(args[0], args[1], args[2]);
        main.addUserRequestListenerFor(auctionHouse);
    }

    private void addUserRequestListenerFor(AuctionHouse auctionHouse) {
        ui.addUserRequestListener(new SniperLauncher(portfolio, auctionHouse));
    }

}
