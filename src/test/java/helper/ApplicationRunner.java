package helper;

import mock.auction.FakeAuctionServer;
import sniper.Main;
import sniper.MainWindow;

public class ApplicationRunner {
    public static final String SNIPER_ID = "sniper";
    private static final String SNIPER_PASSWORD = "sniper";

    private AuctionSniperDriver driver; // gui test driver
    private String itemId;


    public void startBiddingIn(FakeAuctionServer auction) {
        itemId = auction.getItemId();
        Main.main(FakeAuctionServer.XMPP_HOSTNAME, SNIPER_ID, SNIPER_PASSWORD, auction.getItemId());
        driver = new AuctionSniperDriver();
        driver.showSniperStatus(MainWindow.STATUS_JOINING);

    }

    public void showsSniperHasLostAuction() {
        driver.showSniperStatus(MainWindow.STATUS_LOST);
    }

    public void stop() {
        if (driver != null) {
            driver.dispose();
        }
    }

    public void hasShownSniperIsBidding(int lastPrice, int lastBid) {
        driver.showSniperStatus(itemId, lastPrice, lastBid, MainWindow.STATUS_BIDDING);
    }

    public void hasShownSniperIsWinning(int winningBid) {
        driver.showSniperStatus(itemId, winningBid, winningBid, MainWindow.STATUS_WINNING);
    }

    public void showsSniperHasWonAuction(int lastPrice) {
        driver.showSniperStatus(itemId, lastPrice, lastPrice, MainWindow.STATUS_WON);
    }
}
