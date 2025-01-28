import mock.auction.FakeAuctionServer;

public class ApplicationRunner {
    private static final String SNIPER_ID = "";
    private static final String SNIPER_PASSWORD = "";
    private static final String STATUS_JOINING = "joining";
    private static final String STATUS_LOST = "lost";
    private AuctionSniperDriver driver; // gui test driver


    public void startBiddingIn(FakeAuctionServer auction) {
        Main.main(FakeAuctionServer.XMPP_HOSTNAME, SNIPER_ID, SNIPER_PASSWORD, auction.getItemId());
        Main main = new Main();
        driver = new AuctionSniperDriver();
        driver.showSniperStatus(STATUS_JOINING);
    }

    public void showsSniperHasLostAuction() {
        driver.showSniperStatus(STATUS_LOST);
    }

    public void stop() {
        if (driver != null) {
            driver.dispose();
        }
    }
}
