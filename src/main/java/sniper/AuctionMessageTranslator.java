package sniper;

import xmpp.Chat;
import xmpp.Message;
import xmpp.MessageListener;

import java.util.HashMap;
import java.util.Map;

public class AuctionMessageTranslator implements MessageListener {
    private final AuctionEventListener listener;
    private final String sniperId;
    public AuctionMessageTranslator(String sniperId, AuctionEventListener listener) {
        this.listener = listener;
        this.sniperId = sniperId;
    }

    @Override
    public void processMessage(Chat chat, Message message) {
        AuctionEvent event = AuctionEvent.from(message.getBody());
        String type = event.type();
        if ("CLOSE".equals(type)) {
            listener.auctionClosed();
        } else if ("PRICE".equals(type)) {
            listener.currentPrice(event.currentPrice(), event.increment(), event.isFrom(sniperId));
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

        public AuctionEventListener.PriceSource isFrom(String sniperId) {
            return bidder().equals(sniperId) ? AuctionEventListener.PriceSource.FromSniper
                    : AuctionEventListener.PriceSource.FromOtherBidder;
        }

        private String bidder() {
            return get("Bidder");
        }
    }
}
