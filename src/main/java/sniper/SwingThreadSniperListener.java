package sniper;

public class SwingThreadSniperListener implements SniperListener {

    private final SnipersTableModel snipers;

    public SwingThreadSniperListener(SnipersTableModel snipers) {
        this.snipers = snipers;
    }

    @Override
    public void sniperStateChanged(SniperSnapshot newSnapshot) {
        snipers.sniperStateChanged(newSnapshot);
    }
}
