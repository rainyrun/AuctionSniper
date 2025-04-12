package sniper;

public class MainWindow {
    public static SniperPortfolio portfolio;
    private static UserRequestListener userRequestListener;

    public MainWindow(SniperPortfolio portfolio) {
        SnipersTableModel snipersTableModel = new SnipersTableModel();
        portfolio.addPortfolioListener(snipersTableModel);
        MainWindow.portfolio = portfolio;
    }

    public static void addItem(String itemId, int stopPrice) {
        userRequestListener.joinAuction(new Item(itemId, stopPrice));
    }

    public static void addUserRequestListener(UserRequestListener userRequestListener) {
        MainWindow.userRequestListener = userRequestListener;
    }
}
