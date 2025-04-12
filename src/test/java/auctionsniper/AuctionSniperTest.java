package auctionsniper;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.Mockito;
import sniper.*;
import sniper.Auction;

import static org.mockito.Mockito.*;

public class AuctionSniperTest {
    private static final String ITEM_ID = "item-12345";

    private SniperListener sniperListener = Mockito.mock(SniperListener.class);
    private Auction auction = Mockito.mock(Auction.class);
    private AuctionSniper sniper = new AuctionSniper(auction, new Item(ITEM_ID, 2000));

    @BeforeEach
    void init() {
        sniper.addSniperListener(sniperListener);
    }

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

        verify(auction).bid(bid);
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

    @Test
    void doseNotBidAndReportsLosingIsSubSequentPriceIsAboveStopPrice() {
        sniper.currentPrice(123, 45, AuctionEventListener.PriceSource.FromOtherBidder);
        sniper.currentPrice(2345, 24, AuctionEventListener.PriceSource.FromOtherBidder);

        int bid = 123 + 45;
        verify(sniperListener, atLeast(1)).sniperStateChanged(aSniperThatIs(SniperState.BIDDING));
        verify(auction, atLeast(1)).bid(bid);
        verify(sniperListener).sniperStateChanged(new SniperSnapshot(ITEM_ID, 2345, bid, SniperState.LOSING));
    }

    @Test
    void doseNotBidAndReportLosingIfFirstPriceIsAboveStopPrice() {
        sniper.currentPrice(2345, 24, AuctionEventListener.PriceSource.FromOtherBidder);

        verify(sniperListener, atLeast(1)).sniperStateChanged(aSniperThatIs(SniperState.LOSING));
        verify(auction, never()).bid(anyInt());
    }

    @Test
    void reportsLostIfAuctionClosesWhenLosing() {
        sniper.currentPrice(2345, 24, AuctionEventListener.PriceSource.FromOtherBidder);
        sniper.auctionClosed();

        verify(sniperListener, atLeast(1)).sniperStateChanged(aSniperThatIs(SniperState.LOSING));
        verify(auction, never()).bid(anyInt());
        verify(sniperListener, atLeast(1)).sniperStateChanged(aSniperThatIs(SniperState.LOST));
    }

    @Test
    void continuesToBeLosingOnceStopPriceHasBeenReached() {
        sniper.currentPrice(2345, 24, AuctionEventListener.PriceSource.FromOtherBidder);
        sniper.currentPrice(2369, 24, AuctionEventListener.PriceSource.FromOtherBidder);

        verify(sniperListener, atLeast(2)).sniperStateChanged(aSniperThatIs(SniperState.LOSING));
        verify(auction, never()).bid(anyInt());
    }

    @Test
    void doesNotBidAndReportsLosingIfPriceAfterWinningIsAboveStopPrice() {
        sniper.currentPrice(123, 45, AuctionEventListener.PriceSource.FromSniper);
        sniper.currentPrice(2369, 24, AuctionEventListener.PriceSource.FromOtherBidder);

        verify(sniperListener).sniperStateChanged(aSniperThatIs(SniperState.WINNING));
        verify(auction, never()).bid(anyInt());
        verify(sniperListener).sniperStateChanged(aSniperThatIs(SniperState.LOSING));
    }
}
