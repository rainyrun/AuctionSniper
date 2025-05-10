package helper;

import mock.auction.FakeAuctionServer;
import sniper.AuctionLogDriver;
import sniper.Main;
import sniper.SniperState;

import java.io.IOException;
import java.util.Arrays;

import static mock.auction.FakeAuctionServer.XMPP_HOSTNAME;

public class ApplicationRunner {
    public static final String SNIPER_ID = "sniper";
    private static final String SNIPER_PASSWORD = "sniper";
    private AuctionLogDriver logDriver = new AuctionLogDriver();

    private AuctionSniperDriver driver = new AuctionSniperDriver();; // gui test driver

    public void startBiddingIn(FakeAuctionServer... auctions) {
        startSniper(auctions);
        for (FakeAuctionServer auction : auctions) {
            String itemId = auction.getItemId();
            driver.startBiddingFor(itemId, Integer.MAX_VALUE);
            driver.showSniperStatus(itemId, 0, 0, SniperState.JOINING);
        }
    }

    private void startSniper(FakeAuctionServer[] auctions) {
        Main.main(arguments(auctions));
    }

    private String[] arguments(FakeAuctionServer... auctions) {
        String[] arguments = new String[auctions.length + 3];
        arguments[0] = XMPP_HOSTNAME;
        arguments[1] = SNIPER_ID;
        arguments[2] = SNIPER_PASSWORD;
        for (int i = 0; i < auctions.length; i++) {
            arguments[i + 3] = auctions[i].getItemId();
        }
        return arguments;
    }

    public void showsSniperHasLostAuction(FakeAuctionServer auction, int lastPrice, int lastBid) {
        driver.showSniperStatus(auction.getItemId(), lastPrice, lastBid, SniperState.LOST);
    }

    public void stop() {
        if (driver != null) {
            driver.dispose();
        }
    }

    public void hasShownSniperIsBidding(FakeAuctionServer auction, int lastPrice, int lastBid) {
        driver.showSniperStatus(auction.getItemId(), lastPrice, lastBid, SniperState.BIDDING);
    }

    public void hasShownSniperIsWinning(FakeAuctionServer auction, int winningBid) {
        driver.showSniperStatus(auction.getItemId(), winningBid, winningBid, SniperState.WINNING);
    }

    public void showsSniperHasWonAuction(FakeAuctionServer auction, int lastPrice) {
        driver.showSniperStatus(auction.getItemId(), lastPrice, lastPrice, SniperState.WON);
    }

    public void startBiddingWithStopPrice(int stopPrice, FakeAuctionServer auction) {
        logDriver.clearLog();
        startSniper(new FakeAuctionServer[]{auction});
        String itemId = auction.getItemId();
        driver.startBiddingFor(itemId, stopPrice);
        driver.showSniperStatus(itemId, 0, 0, SniperState.JOINING);
    }

    public void hasShownSniperIsLosing(FakeAuctionServer auction, int lastPrice, int lastBid) {
        driver.showSniperStatus(auction.getItemId(), lastPrice, lastBid, SniperState.LOSING);
    }

    public void reportsInvalidMessage(FakeAuctionServer auction, String brokenMessage) throws IOException {
        logDriver.hasEntry(brokenMessage);

    }

    public void showsSniperHasFailed(FakeAuctionServer auction) {
        driver.showSniperStatus(auction.getItemId(), 0, 0, SniperState.FAILED);
    }
}
