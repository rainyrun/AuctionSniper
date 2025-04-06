package mock.auction;

import xmppmock.Chat;
import xmppmock.Message;
import xmppmock.MessageListener;
import org.junit.jupiter.api.Assertions;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

public class SingleMessageListener implements MessageListener {
    private final ArrayBlockingQueue<Message> messages = new ArrayBlockingQueue<>(1);

    public void processMessage(Chat chat, Message message) {
        messages.add(message);
    }
    public void receiveAMessage(Function<? super String, Boolean> messageMatcher) throws InterruptedException {
        Message message = messages.poll(5, TimeUnit.SECONDS);
        Assertions.assertNotNull(message);
        Assertions.assertTrue(messageMatcher.apply(message.getBody()));
    }
}
