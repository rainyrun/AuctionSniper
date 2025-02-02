package helper;

import org.junit.jupiter.api.Assertions;
import sniper.Main;
import sniper.MainWindow;

public class AuctionSniperDriver {
    public AuctionSniperDriver() {
    }

    public void showSniperStatus(String status) {
        Assertions.assertEquals(status, MainWindow.sniperStatus);
    }

    public void dispose() {

    }
}
