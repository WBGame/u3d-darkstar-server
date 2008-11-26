package ar.com.cubenet.server.leasson6;

import java.io.Serializable;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.sun.sgs.app.AppContext;
import com.sun.sgs.app.AppListener;
import com.sun.sgs.app.ClientSession;
import com.sun.sgs.app.ClientSessionListener;
import com.sun.sgs.app.Delivery;
import com.sun.sgs.app.ManagedObject;

/**
 * AppListener implementation for the leasson6.
 * 
 * @author Sebastián Perruolo
 *
 */
public class TaskAppListener implements Serializable, AppListener, 
		ManagedObject {

	/** The version of the serialized form of this class. */
	private static final long serialVersionUID = 5016546624112206516L;

	/** The {@link Logger} for this class. */
	private static final Logger LOGGER = Logger.getLogger(
			TaskAppListener.class.getName()
	);

	/** A channel which schedule tasks . */
	public static final String TASK_CHANNEL_NAME = "Channel";
	
    /**
     * Notifies this listener that the application has been started
     * for the first time.  This gives the application an opportunity
     * to create a Channel instance.
     *
     * @param props application-specific configuration properties
     */
	@Override
	public final void initialize(final Properties props) {

		AppContext.getChannelManager().createChannel(TASK_CHANNEL_NAME, 
				new TasksChannelListener(),	//comportamiento básico 
				Delivery.RELIABLE);
	}

	/**
     * Notifies this listener that the specified client session has
     * logged in, and returns a {@link ClientSessionListenerImpl} for that
     * session.
     * 
     * @param session a client session
     * @return a (serializable) listener for the client session,
     * or {@code null} to indicate that the session should
     * be terminated without completing the login process.
	 */
	@Override
	public final ClientSessionListener loggedIn(final ClientSession session) {
		LOGGER.log(Level.INFO, "User {0} has logged in", session.getName());
		return new ClientSessionListenerImpl(session);
	}

}
