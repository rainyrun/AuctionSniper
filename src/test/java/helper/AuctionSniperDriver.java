package helper;

import sniper.MainWindow;
import sniper.SniperSnapshot;
import sniper.SniperState;

public class AuctionSniperDriver {
    public AuctionSniperDriver() {
    }

    public void showSniperStatus(String itemId, int lastPrice, int lastBid, SniperState state) {
        SniperSnapshot snapshot = new SniperSnapshot(itemId, lastPrice, lastBid, state);
        MainWindow.portfolio.getPortfolioListener().rowMatching(snapshot, snapshot::isForSameItemAs);
    }

    public void dispose() {

    }

    public void startBiddingFor(String itemId, int stopPrice) {
        MainWindow.addItem(itemId, stopPrice);
    }
}
