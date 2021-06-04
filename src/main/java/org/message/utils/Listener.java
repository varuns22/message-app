package org.message.utils;

/**
 * This interface is used to subscribe to message queue for receiving the
 * messages.
 *
 * @author Varun Srivastava
 */
public interface Listener {

	/**
	 * The method is the consumer of the message.
	 * 
	 * @param message
	 * @throws InterruptedException
	 */
	void onMessageReceived(String message) throws InterruptedException;
}
