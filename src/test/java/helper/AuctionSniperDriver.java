package helper;

import org.junit.jupiter.api.Assertions;
import sniper.MainWindow;

public class AuctionSniperDriver {
    public AuctionSniperDriver() {
    }

    public void showSniperStatus(String status) {
        Assertions.assertEquals(status, MainWindow.snipers.getStatusText());
    }

    public void showSniperStatus(String itemId, int lastPrice, int lastBid, String status) {
        Assertions.assertEquals(itemId, MainWindow.snipers.getItemId());
        Assertions.assertEquals(lastPrice, MainWindow.snipers.getLastPrice());
        Assertions.assertEquals(lastBid, MainWindow.snipers.getLastBid());
        Assertions.assertEquals(status, MainWindow.snipers.getStatusText());
    }

    public void dispose() {

    }
}
