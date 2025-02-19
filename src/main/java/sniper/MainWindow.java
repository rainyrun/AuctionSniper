package sniper;

public class MainWindow {
    public static SnipersTableModel snipers;
    private static UserRequestListener userRequestListener;

    public MainWindow(SnipersTableModel snipers) {
        MainWindow.snipers = snipers;
    }

    public static void addItem(String itemId) {
        userRequestListener.joinAuction(itemId);
    }

    public static void addUserRequestListener(UserRequestListener userRequestListener) {
        MainWindow.userRequestListener = userRequestListener;
    }
}
