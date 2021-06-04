/**
 * 
 */
package org.message;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.message.queue.MessageQueue;

/**
 * @author Varun Srivastava
 *
 */
public class MessageApplication {

	private static final String INITIATOR = "initiator";

	private static MessageQueue messageQueue = new MessageQueue();

	private static String playerName;
	private static String startMessage;

	public static Option helpOption = Option.builder("h").longOpt("help").required(false).hasArg(false).desc("Help")
			.build();

	public static Option playerNameOption = Option.builder("p").longOpt("player").required(true).hasArg(true)
			.desc("Player 2 Name").build();

	public static Option startMessageOption = Option.builder("m").longOpt("message").required(true).hasArg(true)
			.desc("Start Message").build();

	public static void main(String[] args) {

		
		
		initialize(args);

		Player initiator = new Player(INITIATOR);
		Player receiver = new Player(playerName);

		// subscribe the players to the message queue
		messageQueue.subscribe(initiator);
		messageQueue.subscribe(receiver);

		try {
			// send the first message to the second player
			initiator.sendMessage(startMessage);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (messageQueue.isEmpty()) {
			System.out.println();
			System.out.println("Shutting down ...");
			System.exit(0);
		}

	}

	/**
	 * Initialises the player name and message passed as arguments
	 * 
	 * @param args
	 */
	private static void initialize(String[] args) {
		HelpFormatter formatter = new HelpFormatter();
		CommandLineParser parser = new DefaultParser();

		Options options = new Options();
		options.addOption(playerNameOption);
		options.addOption(startMessageOption);
		options.addOption(helpOption);

		try {
			if (checkHelpOption(args)) {
				formatter.printHelp("Parameter Information", options);
				System.exit(0);
			}

			System.out.println("Starting Application ...");
			System.out.println();
			CommandLine commandLine = parser.parse(options, args);

			playerName = commandLine.getOptionValue("player");
			startMessage = commandLine.getOptionValue("message");

		} catch (ParseException e) {
			System.out.println(e.getMessage());
			formatter.printHelp("Parameters Required", options);
			System.exit(0);
		}

	}

	/**
	 * 
	 * Check the help option parameter in argument
	 * 
	 * @param args
	 * @return true if help option is used in the argument
	 * @throws ParseException
	 */
	private static boolean checkHelpOption(String[] args) throws ParseException {

		Options options = new Options();
		options.addOption(playerNameOption);
		options.addOption(startMessageOption);
		options.addOption(helpOption);

		try {

			CommandLineParser helpParser = new DefaultParser();

			CommandLine cmd = helpParser.parse(options, args, false);

			if (cmd.hasOption(helpOption.getOpt())) {
				return true;
			}

		} catch (ParseException e) {
			throw e;
		}

		return false;
	}

}
