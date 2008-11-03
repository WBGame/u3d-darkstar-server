package ar.com.cubenet.server.leasson3;

import java.io.Serializable;
import java.nio.ByteBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.unicen.u3d.common.tarea1.Serializer;

import com.sun.sgs.app.AppContext;
import com.sun.sgs.app.Channel;
import com.sun.sgs.app.ChannelListener;
import com.sun.sgs.app.ClientSession;

/**
 * Simple example {@link ChannelListener} for the Project Darkstar Server.
 * <p>
 * Logs when a channel receives data.
 */
class ServerChannelsBroadcastListener
implements Serializable, ChannelListener {
	/** The version of the serialized form of this class. */
	private static final long serialVersionUID = 1L;

	/** The {@link Logger} for this class. */
	private static final Logger logger = Logger.getLogger(ServerChannelsBroadcastListener.class.getName());

	/**
	 * {@inheritDoc}
	 * <p>
	 * Logs when data arrives on a channel. A typical listener would 
	 * examine the message to decide whether it should be discarded, 
	 * modified, or sent unchanged.
	 */
	public void receivedMessage(Channel channel, ClientSession session, ByteBuffer message){
		if (logger.isLoggable(Level.INFO)) {
			logger.log(Level.INFO, "Channel message from {0} on channel {1}", new Object[] { session.getName(), channel.getName() }	);
		}
		String decodedMessage = Serializer.decodeString(message);
		if((decodedMessage!=null)&&(decodedMessage.startsWith("/"))){
			if(decodedMessage.startsWith("/leave")){
				session.send(Serializer.encodeString("You request to leave " + channel.getName() + " channel"));
				channel.leave(session);
			}
		}else{
			channel.send(session, message);
			String channelNameToBroadcast = ServerChannels.CHANNEL_2_NAME;
			logger.log(Level.INFO, "Broadcasting message from to channel {0}", channelNameToBroadcast	);
			AppContext.getChannelManager().getChannel(channelNameToBroadcast).send(session, message);
		}

	}

}
