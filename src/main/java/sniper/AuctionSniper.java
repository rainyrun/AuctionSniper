package sniper;

public class AuctionSniper implements AuctionEventListener {
    private SniperListener sniperListener;
    private Auction auction;
    private SniperSnapshot snapshot;

    public AuctionSniper(Auction auction, String itemId) {
        this.auction = auction;
        this.snapshot = SniperSnapshot.joining(itemId);
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

    public String getItemId() {
        return snapshot.itemId;
    }

    public SniperSnapshot getSnapshot() {
        return snapshot;
    }

    public void addSniperListener(SniperListener sniperListener) {
        this.sniperListener = sniperListener;
        notifyChange();
    }
}
