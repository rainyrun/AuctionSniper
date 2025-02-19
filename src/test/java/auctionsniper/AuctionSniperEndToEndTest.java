package auctionsniper;

import helper.ApplicationRunner;
import mock.auction.FakeAuctionServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

public class AuctionSniperEndToEndTest {
    private final FakeAuctionServer auction = new FakeAuctionServer("item-12345");
    private final FakeAuctionServer auction2 = new FakeAuctionServer("item-65432");
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
        application.showsSniperHasLostAuction(auction, 0, 0);
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
        application.hasShownSniperIsBidding(auction, 1000, 1098);
        // expected
        auction.hasReceivedBid(1098, ApplicationRunner.SNIPER_ID);

        // action
        auction.announceClosed();
        // expected
        application.showsSniperHasLostAuction(auction, 1000, 1098);
    }

    @Test
    void sniperWinsAnAuctionByBiddingHigher() throws InterruptedException {
        auction.startSellingItem();

        application.startBiddingIn(auction);
        auction.hasReceivedJoinRequestFromSniper(ApplicationRunner.SNIPER_ID);

        auction.reportPrice(1000, 98, "other bidder");
        application.hasShownSniperIsBidding(auction, 1000, 1098);
        auction.hasReceivedBid(1098, ApplicationRunner.SNIPER_ID);

        auction.reportPrice(1098, 97, ApplicationRunner.SNIPER_ID);
        application.hasShownSniperIsWinning(auction, 1098);

        auction.announceClosed();
        application.showsSniperHasWonAuction(auction, 1098);
    }


    @Test
    void sniperBidsForMultipleItems() throws InterruptedException {
        auction.startSellingItem();
        auction2.startSellingItem();

        application.startBiddingIn(auction, auction2);
        auction.hasReceivedJoinRequestFromSniper(ApplicationRunner.SNIPER_ID);
        auction2.hasReceivedJoinRequestFromSniper(ApplicationRunner.SNIPER_ID);

        auction.reportPrice(1000, 98, "other bidder");
//        application.hasShownSniperIsBidding(auction, 1000, 1098);
        auction.hasReceivedBid(1098, ApplicationRunner.SNIPER_ID);

        auction2.reportPrice(500, 21, "other bidder");
//        application.hasShownSniperIsBidding(auction2, 500, 521);
        auction2.hasReceivedBid(521, ApplicationRunner.SNIPER_ID);

        auction.reportPrice(1098, 97, ApplicationRunner.SNIPER_ID);
        auction2.reportPrice(521, 22, ApplicationRunner.SNIPER_ID);
        application.hasShownSniperIsWinning(auction, 1098);
        application.hasShownSniperIsWinning(auction2, 521);

        auction.announceClosed();
        auction2.announceClosed();
        application.showsSniperHasWonAuction(auction, 1098);
        application.showsSniperHasWonAuction(auction2, 521);
    }

    @AfterEach
    void stop() {
        auction.stop();
        application.stop();
    }
}