package ar.com.cubenet.server.leasson6;

import java.io.Serializable;
import java.nio.ByteBuffer;

import ar.com.cubenet.common.leasson3.Serializer;
import ar.com.cubenet.server.leasson6.tasks.ComplexTask;

import com.sun.sgs.app.AppContext;
import com.sun.sgs.app.Channel;
import com.sun.sgs.app.ChannelListener;
import com.sun.sgs.app.ClientSession;

/**
 * A channel listener that create and enqueue a ComplexTask when
 * a user send a message.
 * 
 * @author Sebastián Perruolo
 *
 */
public class TasksChannelListener implements Serializable, ChannelListener {

	/** The version of the serialized form of this class. */
	private static final long serialVersionUID = -6313569373211955782L;

	/**
     * Notifies this listener that the given {@code message} is being sent
     * by the specified {@code sender} on the given {@code channel}.
     * 
     * @param	channel a channel
     * @param	sender the sending client session
     * @param	message a message
	 */
	@Override
	public final void receivedMessage(final Channel channel, 
			final ClientSession sender, final ByteBuffer message) {
		String decodedMessage = Serializer.decodeString(message);
		AppContext.getTaskManager().scheduleTask(
				new ComplexTask(sender, decodedMessage)
		);
	}

}
