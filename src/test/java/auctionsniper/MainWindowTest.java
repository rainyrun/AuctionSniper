package auctionsniper;

import helper.AuctionSniperDriver;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import sniper.Item;
import sniper.MainWindow;
import sniper.SniperPortfolio;
import sniper.UserRequestListener;

public class MainWindowTest {
    private final SniperPortfolio portfolio = new SniperPortfolio();
    private final MainWindow mainWindow = new MainWindow(portfolio);
    private final AuctionSniperDriver driver = new AuctionSniperDriver();

    @Test
    public void makesUserRequestWhenJoinButtonClicked() {
        final Item[] myItem = {null};
        mainWindow.addUserRequestListener(new UserRequestListener() {
            @Override
            public void joinAuction(Item item) {
                myItem[0] = item;
            }
        });

        driver.startBiddingFor("an item-id", 789);
        Assertions.assertEquals(new Item("an item-id", 789), myItem[0]);
    }
}
