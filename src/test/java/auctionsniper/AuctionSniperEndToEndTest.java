package auctionsniper;

import helper.ApplicationRunner;
import mock.auction.FakeAuctionServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

public class AuctionSniperEndToEndTest {
    private final FakeAuctionServer auction = new FakeAuctionServer("item-12345");
    private final ApplicationRunner application = new ApplicationRunner();

    @Test
    void sniperJoinsAuctionUntilAuctionCloses() throws InterruptedException {
        // init
        auction.startSellingItem();

        // action
        application.startBiddingIn(auction);
        // expected
        auction.hasReceivedJoinRequestFromSniper(ApplicationRunner.SNIPER_ID);

        // action
        auction.announceClosed();
        // expected
        application.showsSniperHasLostAuction();
    }

    @Test
    void sniperMakesAHigherBidButLoses() throws InterruptedException {
        // init
        auction.startSellingItem();

        // action
        application.startBiddingIn(auction);
        // expected
        auction.hasReceivedJoinRequestFromSniper(ApplicationRunner.SNIPER_ID);

        // action
        auction.reportPrice(1000, 98, "other bidder");
        // expected
        application.hasShownSniperIsBidding();
        // expected
        auction.hasReceivedBid(1098, ApplicationRunner.SNIPER_ID);

        // action
        auction.announceClosed();
        // expected
        application.showsSniperHasLostAuction();
    }

    @AfterEach
    void stop() {
        auction.stop();
        application.stop();
    }
}