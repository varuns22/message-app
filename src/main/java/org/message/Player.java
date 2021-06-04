package org.message;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import org.message.queue.MessageQueue;
import org.message.queue.QueueEvents;
import org.message.utils.Listener;
import org.message.utils.Publisher;

/**
 * The Player is acts as a subscriber and a publisher of messages. It counts the
 * iterations of receiving and sending the message.
 *
 * @author Varun Srivastava
 *
 */
public class Player implements QueueEvents, Listener {

	private final int MAX_MESSAGE_COUNT = 10;

	private final String name;
	private final AtomicReference<MessageQueue> messageQueue = new AtomicReference<MessageQueue>();
	private final AtomicInteger sendCounter = new AtomicInteger();
	private final AtomicInteger receiveCounter = new AtomicInteger();

	public Player(String name) {
		this.name = name;
	}

	public void sendMessage(String message) throws Exception {
		MessageQueue queue = messageQueue.get();

		if (null == queue) {
			throw new Exception("There are no Subscribers to the Message Queue");
		}

		int sendCount = sendCounter.incrementAndGet();

		if (MAX_MESSAGE_COUNT <= sendCount && MAX_MESSAGE_COUNT <= receiveCounter.get()) {
			queue.unsubscribe(this);
		}

		queue.post(new Publisher() {
			@Override
			public String getMessage() {
				String newMessage = message + "," + sendCount;

				System.out.println(String.format(name + " sent message: " + message));

				return newMessage;
			}

			@Override
			public Player getPlayer() {
				return Player.this;
			}
		});

	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		Player player = (Player) o;
		return name.equals(player.name);
	}

	@Override
	public void onMessageReceived(String message) {

		System.out.println(String.format(name + " received message: " + message));

		receiveCounter.incrementAndGet();

		try {
			sendMessage(message);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void subscribe(MessageQueue messageQueue) {
		this.messageQueue.set(messageQueue);
	}

	@Override
	public void unsubscribe(MessageQueue messageQueue) {
		this.messageQueue.set(null);
	}

}
