package sniper;

import java.util.ArrayList;
import java.util.List;

public class SnipersTableModel implements SniperListener {
    private List<SniperSnapshot> snapshots = new ArrayList<>();

    @Override
    public void sniperStateChanged(SniperSnapshot snapshot) {
        int row = rowMatching(snapshot);
        snapshots.set(row, snapshot);
    }

    public int rowMatching(SniperSnapshot snapshot) {
        for (int i = 0; i < snapshots.size(); i++) {
            if (snapshot.isForSameItemAs(snapshots.get(i))) {
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
}
