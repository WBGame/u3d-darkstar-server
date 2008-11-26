/**
 * 
 */
package ar.com.cubenet.server.leasson6;

import java.io.Serializable;
import java.nio.ByteBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.sun.sgs.app.AppContext;
import com.sun.sgs.app.ClientSession;
import com.sun.sgs.app.ClientSessionListener;
import com.sun.sgs.app.ManagedReference;

/**
 * Listener for messages sent from an associated client session to the
 * server.
 * Also join the user to a channel.
 * 
 * @author Sebastián Perruolo
 *
 */
public class ClientSessionListenerImpl implements Serializable,
		ClientSessionListener {

	/** The version of the serialized form of this class. */
	private static final long serialVersionUID = -6538104701920455690L;

	/** The {@link Logger} for this class. */
	private static final Logger LOGGER = Logger.getLogger(
			ClientSessionListenerImpl.class.getName()
	);

	/** The session this {@code ClientSessionListener} is listening to. */
	private final ManagedReference<ClientSession> sessionRef;

	/**
	 * Creates a new {@code ClientSessionListenerImpl} for the session.
	 *
	 * @param session the session this listener is associated with
	 */
	public ClientSessionListenerImpl(final ClientSession session)	{
		if (session == null) {
			throw new NullPointerException("null session");
		}
		sessionRef = AppContext.getDataManager().createReference(session);
		
		AppContext.getChannelManager()
				.getChannel(TaskAppListener.TASK_CHANNEL_NAME)
				.join(session);
	}
	/**
	 * Notifies this listener that the associated session's client has
     * disconnected.
     * 
	 * @param graceful if {@code true}, the specified client
     *        session logged out gracefully
	 * @see com.sun.sgs.app.ClientSessionListener#disconnected(boolean)
	 */
	@Override
	public final void disconnected(final boolean graceful) {

		String grace = "forced";
		if (graceful) {
			grace = "graceful";
		}
		LOGGER.log(
				Level.INFO, 
				"User {0} has logged out {1}", 
				new Object[] { sessionRef.get().getName(), grace }
		);
	}

	/**
	 * Notifies this listener that the specified message, sent by the
	 * associated session's client, was received.
	 *
	 * @param	message a message
	 */
	@Override
	public final void receivedMessage(final ByteBuffer message) {
		//si no es un comando..
		sessionRef.get().send(message);
	}

}
