/**
 * 
 */
package ar.com.cubenet.client.leasson3.listener;

import java.nio.ByteBuffer;
import java.util.concurrent.atomic.AtomicInteger;

import ar.com.cubenet.client.leasson3.AbstractChatClient;
import ar.com.cubenet.common.leasson3.Serializer;

import com.sun.sgs.client.ClientChannel;
import com.sun.sgs.client.ClientChannelListener;

/**
 * A simple listener for channel events.
 * 
 * @author Sebastián Perruolo
 *
 */
public class HelloChannelListener implements ClientChannelListener {

	/** Sequence generator for counting channels. */
	private final AtomicInteger channelNumberSequence =
		new AtomicInteger(1);
	/**
	 * An example of per-channel state, recording the number of
	 * channel joins when the client joined this channel.
	 */
	private final int channelNumber;

	/** Instancia de la UI. */
	private AbstractChatClient chatClient;

	/**
	 * Creates a new {@code HelloChannelListener}. Note that
	 * the listener will be given the channel on its callback
	 * methods, so it does not need to record the channel as
	 * state during the join.
	 * 
	 * @param aChatClient .
	 */
	public HelloChannelListener(final AbstractChatClient aChatClient) {
		channelNumber = channelNumberSequence.getAndIncrement();
		chatClient = aChatClient;
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * Displays a message when this client leaves a channel.
	 */
	public final void leftChannel(final ClientChannel channel) {
		chatClient.appendOutput("Removed from channel " + channel.getName());
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * Formats and displays messages received on a channel.
	 */
	public final void receivedMessage(final ClientChannel channel, 
			final ByteBuffer message) {
		chatClient.appendOutput("[" + channel.getName() + "/ " + channelNumber 
				+ "] " + Serializer.decodeString(message));
	}

}
