package sniper;

public class SniperLauncher implements UserRequestListener {
    private final SniperCollector snipers;
    private final AuctionHouse auctionHouse;

    public SniperLauncher(SniperCollector snipers, AuctionHouse auctionHouse) {
        this.snipers = snipers;
        this.auctionHouse = auctionHouse;
    }

    @Override
    public void joinAuction(String itemId) {
        Auction auction = auctionHouse.auctionFor(itemId);
        AuctionSniper sniper = new AuctionSniper(auction, itemId);
        auction.addAuctionEventListener(sniper);
        snipers.addSniper(sniper);
        auction.join();
    }
}
