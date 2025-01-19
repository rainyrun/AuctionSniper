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
        auction.hasReceivedJoinRequestFromSniper();

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