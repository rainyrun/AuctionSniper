package mock.auction;

import mock.xmpp.Chat;
import mock.xmpp.Message;
import mock.xmpp.MessageListener;
import org.junit.jupiter.api.Assertions;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

public class SingleMessageListener implements MessageListener {
    private final ArrayBlockingQueue<Message> messages = new ArrayBlockingQueue<>(1);

    public void processMessage(Chat chat, Message message) {
        messages.add(message);
    }
    public void receiveAMessage() throws InterruptedException {
        Assertions.assertNotNull(messages.poll(5, TimeUnit.SECONDS));
    }
}
