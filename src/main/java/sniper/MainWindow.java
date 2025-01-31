package sniper;

public class MainWindow {
    public static final String STATUS_JOINING = "joining";
    public static final String STATUS_LOST = "lost";
    public static final String STATUS_BIDDING = "bidding";

    private String sniperStatus = STATUS_JOINING;

    public String getStatus() {
        return sniperStatus;
    }

    public void showStatus(String status) {
        sniperStatus = status;
    }
}
