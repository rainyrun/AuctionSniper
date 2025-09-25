package sniper;

import auction.AuctionSniper;
import ui.PortfolioListener;

import java.util.ArrayList;
import java.util.List;

public class SniperPortfolio implements SniperCollector {
    private List<AuctionSniper> snipers = new ArrayList<>();
    private PortfolioListener portfolioListener;
    public void addPortfolioListener(PortfolioListener snipersTableModel) {
        this.portfolioListener = snipersTableModel;
    }

    @Override
    public void addSniper(AuctionSniper sniper) {
        snipers.add(sniper);
        portfolioListener.sniperAdded(sniper);
    }

    public SnipersTableModel getPortfolioListener() {
        return (SnipersTableModel) portfolioListener;
    }
}
