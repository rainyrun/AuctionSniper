package auctionsniper;

import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import sniper.*;

import static org.mockito.Mockito.*;

public class SniperLauncherTest {
    private final AuctionHouse auctionHouse = mock(AuctionHouse.class);
    private final Auction auction = mock(Auction.class);
    private final SniperCollector sniperCollector = mock(SniperCollector.class);
    private final SniperLauncher launcher = new SniperLauncher(sniperCollector, auctionHouse);

    @Test
    public void addsNewSniperToCollectorAndThenJoinsAuction() {
        final String itemId = "item 123";
        when(auctionHouse.auctionFor(itemId)).thenReturn(auction);
        launcher.joinAuction(itemId);

        InOrder inOrder = inOrder(auction, sniperCollector);
        inOrder.verify(auction).addAuctionEventListener(sniperForItem(itemId));
        inOrder.verify(sniperCollector).addSniper(sniperForItem(itemId));
        inOrder.verify(auction).join();
    }

    private AuctionSniper sniperForItem(String itemId) {
        return argThat((AuctionSniper sniper) -> sniper.getItemId().equals(itemId));
    }
}
