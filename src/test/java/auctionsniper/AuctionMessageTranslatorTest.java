package auctionsniper;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import sniper.AuctionEventListener;
import sniper.AuctionMessageTranslator;
import sniper.Main;
import xmpp.Chat;
import xmpp.Message;

public class AuctionMessageTranslatorTest {
    public static final Chat UNUSED_CHAT = null;
    private final AuctionEventListener listener = Mockito.mock(AuctionEventListener.class);
    private final AuctionMessageTranslator translator = new AuctionMessageTranslator(listener);

    @Test
    void notifiesAuctionClosedWhenCloseMessageReceived() {
        Message message = new Message(Main.EVENT_CLOSE);
        translator.processMessage(UNUSED_CHAT, message);
        Mockito.verify(listener).auctionClosed();
    }

    @Test
    void notifiesBidDetailsWhenCurrentPriceMessageReceived() {
        Message message = new Message(String.format(Main.EVENT_FORMAT, 192, 7, "Someone else"));
        translator.processMessage(UNUSED_CHAT, message);
        Mockito.verify(listener, Mockito.only()).currentPrice(192, 7);
    }
}
