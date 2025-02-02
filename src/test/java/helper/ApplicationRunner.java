package helper;

import mock.auction.FakeAuctionServer;
import sniper.Main;
import sniper.MainWindow;

public class ApplicationRunner {
    public static final String SNIPER_ID = "sniper";
    private static final String SNIPER_PASSWORD = "sniper";
    private AuctionSniperDriver driver; // gui test driver


    public void startBiddingIn(FakeAuctionServer auction) {
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

    public void hasShownSniperIsBidding() {
        driver.showSniperStatus(MainWindow.STATUS_BIDDING);
    }
}
