package auctionsniper;

import helper.AuctionSniperDriver;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import sniper.MainWindow;
import sniper.SnipersTableModel;
import sniper.UserRequestListener;

public class MainWindowTest {
    private final SnipersTableModel tableModel = new SnipersTableModel();
    private final MainWindow mainWindow = new MainWindow(tableModel);
    private final AuctionSniperDriver driver = new AuctionSniperDriver();

    @Test
    public void makesUserRequestWhenJoinButtonClicked() {
        final String[] actualItemId = {null};
        mainWindow.addUserRequestListener(new UserRequestListener() {
            @Override
            public void joinAuction(String itemId) {
                actualItemId[0] = itemId;
            }
        });

        driver.startBiddingFor("an item-id");
        Assertions.assertEquals("an item-id", actualItemId[0]);
    }
}
