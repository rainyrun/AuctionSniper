import org.junit.jupiter.api.Assertions;

public class AuctionSniperDriver {
    public AuctionSniperDriver() {
    }

    public void showSniperStatus(String status) {
        Assertions.assertEquals(status, Main.main.getStatus());
    }

    public void dispose() {

    }
}
