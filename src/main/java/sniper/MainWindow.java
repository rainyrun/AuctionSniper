package sniper;

public class MainWindow {
    public static final String STATUS_JOINING = "joining";
    public static final String STATUS_LOST = "lost";
    public static final String STATUS_BIDDING = "bidding";
    public static final String STATUS_WINNING = "winning";
    public static final String STATUS_WON = "won";

    public static SnipersTableModel snipers;

    public MainWindow(SnipersTableModel snipers) {
        MainWindow.snipers = snipers;
    }
}
