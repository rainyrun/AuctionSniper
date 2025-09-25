package sniper;

import auction.AuctionSniper;
import ui.PortfolioListener;
import ui.SwingThreadSniperListener;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class SnipersTableModel implements SniperListener, PortfolioListener {
    private List<SniperSnapshot> snapshots = new ArrayList<>();

    @Override
    public void sniperStateChanged(SniperSnapshot snapshot) {
        int row = rowMatching(snapshot, (item) -> snapshot.itemId.equals(item.itemId));
        snapshots.set(row, snapshot);
    }

    public int rowMatching(SniperSnapshot snapshot, Function<SniperSnapshot, Boolean> function) {
        for (int i = 0; i < snapshots.size(); i++) {
            if (function.apply(snapshots.get(i))) {
                return i;
            }
        }
        throw new IllegalArgumentException("Cannot find match for " + snapshot);
    }

    public int getRowCount() {
        return snapshots.size();
    }

    public void addSniper(SniperSnapshot snapshot) {
        snapshots.add(snapshot);
    }

    public SniperSnapshot getRow(int row) {
        return snapshots.get(row - 1);
    }

    private void addSniperSnapshot(SniperSnapshot snapshot) {
        snapshots.add(snapshot);
    }

    @Override
    public void sniperAdded(AuctionSniper sniper) {
        addSniperSnapshot(sniper.getSnapshot());
        sniper.addSniperListener(new SwingThreadSniperListener(this));
    }
}
