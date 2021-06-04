package org.message.queue;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.message.utils.Listener;
import org.message.utils.Publisher;
import org.message.utils.Subscriber;

/**
 * The MessageQueue is implemented using publish-subscribe java pattern used for
 * sending messages to the subscribers.
 *
 * @author Varun Srivastava
 */
public class MessageQueue {

	private List<Subscriber> subscribers = new ArrayList<Subscriber>();

	private Boolean isEmpty = true;

	public void subscribe(Listener listener) {

		subscribers.add(new Subscriber(listener));

		isEmpty = false;

		if (listener instanceof QueueEvents) {
			((QueueEvents) listener).subscribe(this);
		}
	}

	public void unsubscribe(Listener listener) {
		// filter out the player from the list of subscribers
		List<Subscriber> subscribersToRemove = subscribers.stream()
				.filter(subscriber -> listener.equals(subscriber.getListener())).collect(Collectors.toList());

		subscribers.removeAll(subscribersToRemove);

		isEmpty = (subscribers.isEmpty()) ? true : false;

		if (listener instanceof QueueEvents) {
			((QueueEvents) listener).unsubscribe(this);
		}
	}

	public void post(Publisher publisher) {
		// filter out the sender from the list of subscribers
		List<Subscriber> filteredSubscribers = subscribers.stream()
				.filter(sender -> sender.getListener() != publisher.getPlayer()).collect(Collectors.toList());

		// send message to the subscribers in the queue
		for (Subscriber subscriber : filteredSubscribers) {
			subscriber.dispatchMessage(publisher.getMessage());
		}
	}

	public boolean isEmpty() {
		return isEmpty;
	}

}
