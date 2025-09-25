package ui;

import sniper.SniperListener;
import sniper.SniperSnapshot;
import sniper.SnipersTableModel;

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
