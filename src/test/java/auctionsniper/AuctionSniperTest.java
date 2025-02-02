package auctionsniper;

import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.Mockito;
import sniper.Auction;
import sniper.AuctionEventListener;
import sniper.AuctionSniper;
import sniper.SniperListener;
import static org.mockito.Mockito.*;

public class AuctionSniperTest {
    private SniperListener sniperListener = Mockito.mock(SniperListener.class);
    private Auction auction = Mockito.mock(Auction.class);
    private AuctionSniper sniper = new AuctionSniper(sniperListener, auction);

    @Test
    void reportsLostWhenAuctionCloseImmediately() {
        sniper.auctionClosed();

        Mockito.verify(sniperListener, atLeast(1)).sniperLost();
    }

    @Test
    void bidsHigherAndReportsBiddingWhenNewPriceArrives() {
        int price = 1001;
        int increment = 25;
        sniper.currentPrice(price, increment, AuctionEventListener.PriceSource.FromOtherBidder);

        Mockito.verify(auction).bid(price + increment);
        verify(sniperListener, atLeast(1)).sniperBidding();
    }

    @Test
    void reportsIsWinnerWhenCurrentPriceComesFromSniper() {
        sniper.currentPrice(123, 45, AuctionEventListener.PriceSource.FromSniper);

        verify(sniperListener, atLeast(1)).sniperWinning();
    }

    @Test
    void reportsLostIfAuctionClosesWhenBidding() {
        sniper.currentPrice(123, 45, AuctionEventListener.PriceSource.FromOtherBidder);
        sniper.auctionClosed();

        InOrder inOrder = inOrder(sniperListener);
        inOrder.verify(sniperListener, atLeastOnce()).sniperBidding();
        inOrder.verify(sniperListener, atLeastOnce()).sniperLost();
    }

    @Test
    void reportsWonIfAuctionClosesWhenWinning() {
        sniper.currentPrice(123, 45, AuctionEventListener.PriceSource.FromSniper);
        sniper.auctionClosed();

        InOrder inOrder = inOrder(sniperListener);
        inOrder.verify(sniperListener, atLeastOnce()).sniperWinning();
        inOrder.verify(sniperListener, atLeastOnce()).sniperWon();
    }

    @Test
    void reportsLostIfAuctionClosesWhenBiddingAfterWinning() {
        sniper.currentPrice(122, 21, AuctionEventListener.PriceSource.FromOtherBidder);
        sniper.currentPrice(143, 45, AuctionEventListener.PriceSource.FromSniper);
        sniper.currentPrice(155, 11, AuctionEventListener.PriceSource.FromOtherBidder);
        sniper.auctionClosed();

        InOrder inOrder = inOrder(sniperListener);
        inOrder.verify(sniperListener).sniperBidding();
        inOrder.verify(sniperListener).sniperWinning();
        inOrder.verify(sniperListener).sniperBidding();
        inOrder.verify(sniperListener, atLeastOnce()).sniperLost();
    }
}
