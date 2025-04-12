package sniper;

public class SniperLauncher implements UserRequestListener {
    private final SniperCollector snipers;
    private final AuctionHouse auctionHouse;

    public SniperLauncher(SniperCollector snipers, AuctionHouse auctionHouse) {
        this.snipers = snipers;
        this.auctionHouse = auctionHouse;
    }

    @Override
    public void joinAuction(Item item) {
        Auction auction = auctionHouse.auctionFor(item.itemId);
        AuctionSniper sniper = new AuctionSniper(auction, item);
        auction.addAuctionEventListener(sniper);
        snipers.addSniper(sniper);
        auction.join();
    }
}
