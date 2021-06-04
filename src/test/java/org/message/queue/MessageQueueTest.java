package org.message.queue;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Before;
import org.junit.Test;
import org.message.Player;
import org.message.utils.Listener;
import org.message.utils.Publisher;

/**
 * @author Varun Srivastava
 *
 */
public class MessageQueueTest {

	private MessageQueue queueTest;
	private final String expectedMessage = "TEST";

	@Before
	public void setUp() {
		queueTest = new MessageQueue();
	}

	/**
	 * Add two subscribers to the message queue and check how the message is
	 * received
	 * 
	 */
	@Test
	public void checkRecevicedMessage() {

		AtomicInteger countDown = new AtomicInteger(2);

		queueTest.subscribe(message -> {
			assertEquals(expectedMessage, message);
			countDown.decrementAndGet();
		});
		System.err.println(countDown);
		queueTest.subscribe(message -> {
			assertEquals(expectedMessage, message);
			countDown.decrementAndGet();
		});

		queueTest.post(new DefaultPublisher(expectedMessage));
		System.err.println(countDown);
	}

	/**
	 * Post messages on the message queue and unsubscribe from it through 10 messages.
	 */
	@Test
	public void postInLoop() {

		AtomicInteger countDown = new AtomicInteger(10);

		queueTest.post(new DefaultPublisher(expectedMessage));
		queueTest.subscribe(new Listener() {

			final AtomicInteger counter = new AtomicInteger();

			@Override
			public void onMessageReceived(String message) {
				countDown.decrementAndGet();

				if (10 <= counter.incrementAndGet()) {
					queueTest.unsubscribe(this);
				}
				System.err.println(countDown);
				queueTest.post(new DefaultPublisher(expectedMessage));

				assertEquals(expectedMessage, message);
			}
		});

		queueTest.post(new DefaultPublisher(expectedMessage));

	}

	/**
	 * Sending the message to the queue and unsubscribing the listener. Checking if
	 * the message queue is empty or not
	 */
	@Test
	public void isEmptyBus() {
		AtomicBoolean messageReceived = new AtomicBoolean();
		AtomicInteger countDown = new AtomicInteger(10);
		AtomicBoolean isEmpty = new AtomicBoolean();
		AtomicBoolean onUnsubscribe = new AtomicBoolean();

		queueTest.post(new DefaultPublisher(expectedMessage));
		queueTest.subscribe(new QueueListner() {
			@Override
			public void onMessageReceived(String message) {
				messageReceived.set(true);
				queueTest.unsubscribe(this);
			}

			@Override
			public void unsubscribe(MessageQueue messageQueue) {
				onUnsubscribe.set(true);
			}
		});

		new Thread(() -> {
			while (true) {
				countDown.decrementAndGet();
				if (queueTest.isEmpty()) {
					assertTrue(onUnsubscribe.get());
					isEmpty.set(true);
					break;
				}
			}
		}).start();
	}

	private static class DefaultPublisher implements Publisher {

		private String message;

		private DefaultPublisher(String message) {
			this.message = message;
		}

		@Override
		public String getMessage() {
			return message;
		}

		@Override
		public Player getPlayer() {
			return new Player("Test Player");
		}
	}

	private static class QueueListner implements Listener, QueueEvents {

		@Override
		public void subscribe(MessageQueue messageQueue) {

		}

		@Override
		public void unsubscribe(MessageQueue MessageQueue) {

		}

		@Override
		public void onMessageReceived(String message) throws InterruptedException {

		}
	}
}
