package sniper;

public class SniperStateDisplayer implements SniperListener {
    private MainWindow ui = new MainWindow();

    public SniperStateDisplayer() {
        ui.showStatus(MainWindow.STATUS_JOINING);
    }

    @Override
    public void sniperLost() {
        ui.showStatus(MainWindow.STATUS_LOST);
    }

    @Override
    public void sniperBidding() {
        ui.showStatus(MainWindow.STATUS_BIDDING);
    }

    @Override
    public void sniperWinning() {
        ui.showStatus(MainWindow.STATUS_WINNING);
    }
}
