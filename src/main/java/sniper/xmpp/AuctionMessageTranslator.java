package sniper.xmpp;

import sniper.AuctionEventListener;
import sniper.XMPPFailureReporter;
import xmppmock.Chat;
import xmppmock.Message;
import xmppmock.MessageListener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AuctionMessageTranslator implements MessageListener {
    private final List<AuctionEventListener> listeners;
    private final XMPPFailureReporter failureReporter;
    private final String sniperId;
    public AuctionMessageTranslator(String sniperId, List<AuctionEventListener> listener, XMPPFailureReporter failureReporter) {
        this.listeners = listener;
        this.sniperId = sniperId;
        this.failureReporter = failureReporter;
    }

    @Override
    public void processMessage(Chat chat, Message message) {
        try {
            translate(message);
        } catch (Exception e) {
            failureReporter.cannotTranslateMessage(sniperId, message.getBody(), e);
            for (AuctionEventListener listener : listeners) {
                listener.auctionFailed();
            }
        }
    }

    private void translate(Message message) {
        AuctionEvent event = AuctionEvent.from(message.getBody());
        String type = event.type();
        if ("CLOSE".equals(type)) {
            for (AuctionEventListener listener : listeners) {
                listener.auctionClosed();
            }
        } else if ("PRICE".equals(type)) {
            for (AuctionEventListener listener : listeners) {
                listener.currentPrice(event.currentPrice(), event.increment(), event.isFrom(sniperId));
            }
        }
    }

    private static class AuctionEvent {
        private final Map<String, String> fields = new HashMap<>();

        public String type() {
            return get("Event");
        }

        private String get(String fieldName) {
            String value = fields.get(fieldName);
            if (value == null || value.length() == 0) {
                throw new MissingValueException();
            }
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
