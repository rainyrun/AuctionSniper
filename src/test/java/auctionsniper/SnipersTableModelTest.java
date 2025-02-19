package auctionsniper;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import sniper.SniperSnapshot;
import sniper.SnipersTableModel;

public class SnipersTableModelTest {
    private final SnipersTableModel model = new SnipersTableModel();

    @Test
    void setsSniperValuesInColumns() {
        SniperSnapshot joining = SniperSnapshot.joining("item123");
        SniperSnapshot bidding = joining.bidding(555, 666);
        model.addSniper(joining);
        model.sniperStateChanged(bidding);

        Assertions.assertEquals(bidding, model.getRow(1));
    }

    @Test
    void notifiesListenersWhenAddingASniper() {
        SniperSnapshot joining = SniperSnapshot.joining("item123");
        model.addSniper(joining);
        Assertions.assertEquals(1, model.getRowCount());
    }

    @Test
    void holdsSnipersInAdditionOrder() {
        SniperSnapshot joining1 = SniperSnapshot.joining("item1");
        model.addSniper(joining1);
        SniperSnapshot joining2 = SniperSnapshot.joining("item2");
        model.addSniper(joining2);

        Assertions.assertEquals(model.getRow(1), joining1);
        Assertions.assertEquals(model.getRow(2), joining2);
    }

}
