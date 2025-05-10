package auctionsniper;

import mock.auction.FakeAuctionServer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import sniper.Auction;
import sniper.AuctionEventListener;
import sniper.XMPPAuctionHouse;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class XMPPAuctionHouseTest {
    private final FakeAuctionServer auctionServer = new FakeAuctionServer("item-12345");
    private final XMPPAuctionHouse auctionHouse = XMPPAuctionHouse.connect("localhost", "sniper", "sniper");

    @Test
    public void receivesEventFromAuctionServerAfterJoining() throws InterruptedException {
        auctionServer.startSellingItem();
        Auction auction = auctionHouse.auctionFor(auctionServer.getItemId());
        CountDownLatch auctionWasClosed = new CountDownLatch(1);
        auction.addAuctionEventListener(auctionClosedListener(auctionWasClosed));

        auction.join();
        auctionServer.hasReceivedJoinRequestFromSniper("sniper");
        auctionServer.announceClosed();

        Assertions.assertTrue(auctionWasClosed.await(2, TimeUnit.SECONDS));
    }

    private static AuctionEventListener auctionClosedListener(CountDownLatch auctionWasClosed) {
        return new AuctionEventListener() {

            @Override
            public void auctionClosed() {
                auctionWasClosed.countDown();
            }

            @Override
            public void currentPrice(int price, int increment, PriceSource priceSource) {
            }

            @Override
            public void auctionFailed() {
            }

        };
    }
}
