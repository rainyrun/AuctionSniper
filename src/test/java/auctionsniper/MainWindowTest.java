package auctionsniper;

import helper.AuctionSniperDriver;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import sniper.MainWindow;
import sniper.SniperPortfolio;
import sniper.UserRequestListener;

public class MainWindowTest {
    private final SniperPortfolio portfolio = new SniperPortfolio();
    private final MainWindow mainWindow = new MainWindow(portfolio);
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
