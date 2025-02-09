package sniper;

public class SnipersTableModel implements SniperListener {
    private SniperSnapshot sniperSnapshot =
            new SniperSnapshot("", 0, 0, SniperState.JOINING);

    public String getStatusText() {
        return sniperSnapshot.sniperState.name().toLowerCase();
    }

    public String getItemId() {
        return sniperSnapshot.itemId;
    }

    public int getLastPrice() {
        return sniperSnapshot.lastPrice;
    }

    public int getLastBid() {
        return sniperSnapshot.lastBid;
    }

    @Override
    public void sniperStateChanged(SniperSnapshot sniperSnapshot) {
        this.sniperSnapshot = sniperSnapshot;
    }
}
