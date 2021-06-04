/**
 * 
 */
package org.message.utils;

import org.message.Player;

/**
 * Interface used to send messages to subscribers in the queue.
 *
 * @author Varun Srivastava
 */
public interface Publisher {

	/**
	 * Used to get the message being sent
	 * 
	 * @return String
	 */
	String getMessage();

	/**
	 * Gets the instance of the Player sending the message.
	 * 
	 * @return Player
	 */
	Player getPlayer();
}
