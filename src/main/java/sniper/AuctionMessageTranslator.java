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
        AuctionEvent event = AuctionEvent.from(message.getBody());
        String type = event.type();
        if ("CLOSE".equals(type)) {
            listener.auctionClosed();
        } else if ("PRICE".equals(type)) {
            listener.currentPrice(event.currentPrice(), event.increment());
        }
    }

    private static class AuctionEvent {
        private final Map<String, String> fields = new HashMap<>();

        public String type() {
            return get("Event");
        }

        private String get(String fieldName) {
            return fields.get(fieldName);
        }

        public int currentPrice() {
            return getInt("CurrentPrice");
        }

        private int getInt(String fieldName) {
            return Integer.parseInt(get(fieldName));
        }

        public int increment() {
            return getInt("Increment");
        }

        static AuctionEvent from(String messageBody) {
            AuctionEvent event = new AuctionEvent();
            for (String filed : fieldsIn(messageBody)) {
                event.addField(filed);
            }
            return event;
        }

        private void addField(String filed) {
            String[] pair = filed.split(":");
            fields.put(pair[0].trim(), pair[1].trim());
        }

        private static String[] fieldsIn(String messageBody) {
            return messageBody.split(";");
        }
    }
}
