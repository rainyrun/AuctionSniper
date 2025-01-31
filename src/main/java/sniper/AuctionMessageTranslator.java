package sniper;

import xmpp.Chat;
import xmpp.Message;
import xmpp.MessageListener;

import java.util.HashMap;
import java.util.Map;

public class AuctionMessageTranslator implements MessageListener {
    private final AuctionEventListener listener;
    public AuctionMessageTranslator(AuctionEventListener listener) {
        this.listener = listener;
    }

    @Override
    public void processMessage(Chat chat, Message message) {
        Map<String, String> event = unpackEventFrom(message);
        String type = event.get("Event");
        if ("CLOSE".equals(type)) {
            listener.auctionClosed();
        } else if ("PRICE".equals(type)) {
            listener.currentPrice(Integer.parseInt(event.get("CurrentPrice")),
                    Integer.parseInt(event.get("Increment")));
        }
    }

    private Map<String, String> unpackEventFrom(Message message) {
        Map<String, String> event = new HashMap<>();
        for (String s : message.getBody().split(";")) {
            String[] pair = s.split(":");
            event.put(pair[0].trim(), pair[1].trim());
        }
        return event;
    }
}
