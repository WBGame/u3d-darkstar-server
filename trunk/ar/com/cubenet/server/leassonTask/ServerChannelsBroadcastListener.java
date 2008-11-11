package ar.com.cubenet.server.leassonTask;

import java.io.Serializable;
import java.nio.ByteBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.sun.sgs.app.AppContext;
import com.sun.sgs.app.Channel;
import com.sun.sgs.app.ChannelListener;
import com.sun.sgs.app.ClientSession;

/**
 * Ejemplo simple de un channel que reenvía los mensajes por el mismo channel.
 * a la vez que los reenvía por otro channel definido.
 */
class ServerChannelsBroadcastListener implements Serializable, ChannelListener {
	/** The version of the serialized form of this class. */
	private static final long serialVersionUID = 1L;

	/** The {@link Logger} for this class. */
	private static final Logger logger = Logger.getLogger(
			ServerChannelsBroadcastListener.class.getName()
	);
	
	/**
	 * Channel definido para broadcast.
	 */
	private String channelNameToBroadcast = null;

	/**
	 * @param channelNameToBroadcast nombre del channel por el cual reenviar el 
	 * mensaje.
	 */
	public ServerChannelsBroadcastListener(String channelNameToBroadcast) {
		this.channelNameToBroadcast = channelNameToBroadcast;
	}
	
	/**
	 * Cuando un mensaje llega, este se reenvía por el channel a la vez
	 * que se reenvía por el channel configurado en la creación de esta
	 * instancia.
	 * 
	 * @param channel asdas
	 * @param session asdas
	 * @param message asdsa
	 */
	public void receivedMessage(
			Channel channel, 
			ClientSession session, 
			ByteBuffer message
	) {
		if (logger.isLoggable(Level.INFO)) {
			logger.log(Level.INFO, "Channel message from {0} on channel {1}", new Object[] { session.getName(), channel.getName() });
		}

		channel.send(session, message);
		logger.log(Level.INFO, "Broadcasting message from to channel {0}", channelNameToBroadcast);
		AppContext.getChannelManager().getChannel(channelNameToBroadcast).send(session, message);

	}

}
