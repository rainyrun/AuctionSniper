package sniper;

import xmpp.Chat;

public class XMPPAuction implements Auction {
    private final Chat chat;
    public XMPPAuction(Chat chat) {
        this.chat = chat;
    }

    @Override
    public void bid(int price) {
        sendMessage(String.format(Main.BID_COMMAND_FORMAT, price));
    }

    @Override
    public void join() {
        sendMessage(Main.JOIN_COMMAND_FORMAT);
    }


    private void sendMessage(String message) {
        chat.sendMessage(message);
    }
}
