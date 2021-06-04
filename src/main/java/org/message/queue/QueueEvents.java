package org.message.queue;

/**
 * Interface to be implemented by any listener to be notified about the
 * subscribe/unsubscribe from the message queue.
 *
 * @author Varun Srivastava
 */
public interface QueueEvents {

	/**
	 * Subscribes the listener to the message queue.
	 *
	 */
	void subscribe(MessageQueue messageQueue);

	/**
	 * Removes the listener from the message queue.
	 * 
	 */
	void unsubscribe(MessageQueue MessageQueue);
}