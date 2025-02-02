package auctionsniper;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import sniper.Auction;
import sniper.AuctionSniper;
import sniper.SniperListener;
import static org.mockito.Mockito.*;

public class AuctionSniperTest {
    private SniperListener sniperListener = Mockito.mock(SniperListener.class);
    private Auction auction = Mockito.mock(Auction.class);
    private AuctionSniper sniper = new AuctionSniper(sniperListener, auction);

    @Test
    void reportsLostWhenAuctionClose() {
        sniper.auctionClosed();

        Mockito.verify(sniperListener, atLeast(1)).sniperLost();
    }

    @Test
    void bidsHigherAndReportsBiddingWhenNewPriceArrives() {
        int price = 1001;
        int increment = 25;
        sniper.currentPrice(price, increment);

        Mockito.verify(auction).bid(price + increment);
        verify(sniperListener, atLeast(1)).sniperBidding();
    }
}
