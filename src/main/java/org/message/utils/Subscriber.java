package org.message.utils;

/**
 * Subscriber used to dispatch messages to the Listener.
 *
 * @author Varun Srivastava
 */
public class Subscriber {

	private final Listener listener;

	public Subscriber(Listener listener) {
		this.listener = listener;
	}

	public Listener getListener() {
		return listener;
	}

	public final void dispatchMessage(final String event) {

		try {
			listener.onMessageReceived(event);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
