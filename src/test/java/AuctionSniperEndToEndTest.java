import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

public class AuctionSniperEndToEndTest {
    private final FackAuctionServer auction = new FackAuctionServer("item-12345");
    private final ApplicationRunner application = new ApplicationRunner();

    @Test
    void sniperJoinsAuctionUntilAuctionCloses() {
        // init
        auction.startSellingItem();

        // action
        application.startBiddingIn(auction);
        // expected
        auction.hasReceivedJoinRequestFromSniper();

        // action
        auction.announceCloused();
        // expected
        application.showsSniperHasLostAuction();
    }

    @AfterEach
    void stop() {
        auction.stop();
        application.stop();
    }
}