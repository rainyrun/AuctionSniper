package auctionsniper;

import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.Mockito;
import sniper.*;

import static org.mockito.Mockito.*;

public class AuctionSniperTest {
    private static final String ITEM_ID = "item-12345";

    private SniperListener sniperListener = Mockito.mock(SniperListener.class);
    private Auction auction = Mockito.mock(Auction.class);
    private AuctionSniper sniper = new AuctionSniper(sniperListener, auction, ITEM_ID);

    @Test
    void reportsLostWhenAuctionCloseImmediately() {
        sniper.auctionClosed();

        Mockito.verify(sniperListener, atLeast(1)).sniperStateChanged(
                aSniperThatIs(SniperState.LOST));
    }

    @Test
    void bidsHigherAndReportsBiddingWhenNewPriceArrives() {
        int price = 1001;
        int increment = 25;
        int bid = price + increment;
        sniper.currentPrice(price, increment, AuctionEventListener.PriceSource.FromOtherBidder);

        Mockito.verify(auction).bid(bid);
        verify(sniperListener, atLeast(1)).sniperStateChanged(
                new SniperSnapshot(ITEM_ID, price, bid, SniperState.BIDDING));
    }

    @Test
    void reportsIsWinnerWhenCurrentPriceComesFromSniper() {
        sniper.currentPrice(123, 12, AuctionEventListener.PriceSource.FromOtherBidder);
        sniper.currentPrice(135, 45, AuctionEventListener.PriceSource.FromSniper);

        InOrder inOrder = inOrder(sniperListener);
        inOrder.verify(sniperListener).sniperStateChanged(aSniperThatIs(SniperState.BIDDING));
        inOrder.verify(sniperListener, atLeastOnce()).sniperStateChanged(
                new SniperSnapshot(ITEM_ID, 135, 135, SniperState.WINNING));
    }

    @Test
    void reportsLostIfAuctionClosesWhenBidding() {
        sniper.currentPrice(123, 45, AuctionEventListener.PriceSource.FromOtherBidder);
        sniper.auctionClosed();

        InOrder inOrder = inOrder(sniperListener);
        inOrder.verify(sniperListener, atLeastOnce()).sniperStateChanged(
                aSniperThatIs(SniperState.BIDDING));
        inOrder.verify(sniperListener, atLeastOnce()).sniperStateChanged(
                aSniperThatIs(SniperState.LOST));
    }

    @Test
    void reportsWonIfAuctionClosesWhenWinning() {
        sniper.currentPrice(112, 11, AuctionEventListener.PriceSource.FromOtherBidder);
        sniper.currentPrice(123, 45, AuctionEventListener.PriceSource.FromSniper);
        sniper.auctionClosed();

        InOrder inOrder = inOrder(sniperListener);
        inOrder.verify(sniperListener, atLeastOnce()).sniperStateChanged(
                new SniperSnapshot(ITEM_ID, 123, 123, SniperState.WINNING));
        inOrder.verify(sniperListener, atLeastOnce()).sniperStateChanged(aSniperThatIs(SniperState.WON));
    }

    @Test
    void reportsLostIfAuctionClosesWhenBiddingAfterWinning() {
        sniper.currentPrice(122, 21, AuctionEventListener.PriceSource.FromOtherBidder);
        sniper.currentPrice(143, 45, AuctionEventListener.PriceSource.FromSniper);
        sniper.currentPrice(155, 11, AuctionEventListener.PriceSource.FromOtherBidder);
        sniper.auctionClosed();

        InOrder inOrder = inOrder(sniperListener);
        inOrder.verify(sniperListener).sniperStateChanged(aSniperThatIs(SniperState.BIDDING));
        inOrder.verify(sniperListener).sniperStateChanged(
                new SniperSnapshot(ITEM_ID, 143, 143, SniperState.WINNING));
        inOrder.verify(sniperListener).sniperStateChanged(aSniperThatIs(SniperState.BIDDING));
        inOrder.verify(sniperListener, atLeastOnce()).sniperStateChanged(aSniperThatIs(SniperState.LOST));
    }

    private static SniperSnapshot aSniperThatIs(SniperState sniperState) {
        return argThat((SniperSnapshot snapshot) -> snapshot.sniperState.equals(sniperState));
    }

}
