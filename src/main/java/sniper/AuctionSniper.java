package sniper;

public class AuctionSniper implements AuctionEventListener {
    private SniperListener sniperListener;
    private Auction auction;
    private SniperSnapshot snapshot;

    public AuctionSniper(SniperListener sniperListener, Auction auction, String itemId) {
        this.sniperListener = sniperListener;
        this.auction = auction;
        this.snapshot = SniperSnapshot.joining(itemId);
        notifyChange();
    }

    @Override
    public void auctionClosed() {
        snapshot = snapshot.close();
        notifyChange();
    }

    private void notifyChange() {
        sniperListener.sniperStateChanged(snapshot);
    }

    @Override
    public void currentPrice(int price, int increment, PriceSource priceSource) {
        switch (priceSource) {
            case PriceSource.FromSniper -> snapshot = snapshot.winning(price);
            case PriceSource.FromOtherBidder -> {
                int bid = price + increment;
                auction.bid(bid);
                snapshot = snapshot.bidding(price, bid);
            }
        }
        notifyChange();
    }
}
