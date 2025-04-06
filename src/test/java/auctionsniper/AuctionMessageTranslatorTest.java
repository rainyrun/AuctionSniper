package auctionsniper;

import helper.ApplicationRunner;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import sniper.AuctionEventListener;
import sniper.xmpp.AuctionMessageTranslator;
import sniper.Main;
import xmppmock.Chat;
import xmppmock.Message;

import java.util.Arrays;

public class AuctionMessageTranslatorTest {
    public static final Chat UNUSED_CHAT = null;
    private final AuctionEventListener listener = Mockito.mock(AuctionEventListener.class);
    private final AuctionMessageTranslator translator = new AuctionMessageTranslator(ApplicationRunner.SNIPER_ID, Arrays.asList(listener));

    @Test
    void notifiesAuctionClosedWhenCloseMessageReceived() {
        Message message = new Message(Main.EVENT_CLOSE);
        translator.processMessage(UNUSED_CHAT, message);
        Mockito.verify(listener).auctionClosed();
    }

    @Test
    void notifiesBidDetailsWhenCurrentPriceMessageReceivedFromOtherBidder() {
        Message message = new Message(String.format(Main.EVENT_FORMAT, 192, 7, "Someone else"));
        translator.processMessage(UNUSED_CHAT, message);
        Mockito.verify(listener, Mockito.only()).currentPrice(
                192, 7, AuctionEventListener.PriceSource.FromOtherBidder);
    }

    @Test
    void notifiesBidDetailsWhenCurrentPriceMessageReceivedFromSniper() {
        Message message = new Message(String.format(Main.EVENT_FORMAT, 234, 5, ApplicationRunner.SNIPER_ID));
        translator.processMessage(UNUSED_CHAT, message);
        Mockito.verify(listener, Mockito.only()).currentPrice(
                234, 5, AuctionEventListener.PriceSource.FromSniper);
    }
}
