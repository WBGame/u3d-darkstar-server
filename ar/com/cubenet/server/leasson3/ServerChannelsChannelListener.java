/*
 * Copyright 2007-2008 Sun Microsystems, Inc.
 *
 * This file is part of Project Darkstar Server.
 *
 * Project Darkstar Server is free software: you can redistribute it
 * and/or modify it under the terms of the GNU General Public License
 * version 2 as published by the Free Software Foundation and
 * distributed hereunder to you.
 *
 * Project Darkstar Server is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package ar.com.cubenet.server.leasson3;

import java.io.Serializable;
import java.nio.ByteBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;

import ar.com.cubenet.common.leasson3.Serializer;

import com.sun.sgs.app.Channel;
import com.sun.sgs.app.ChannelListener;
import com.sun.sgs.app.ClientSession;

/**
 * Simple example {@link ChannelListener} for the Project Darkstar Server.
 * <p>
 * Logs when a channel receives data.
 */
class ServerChannelsChannelListener implements Serializable, ChannelListener {
	/** The version of the serialized form of this class. */
	private static final long serialVersionUID = 1L;

	/** The {@link Logger} for this class. */
	private static final Logger logger = Logger.getLogger(ServerChannelsChannelListener.class.getName());

	/**
	 * {@inheritDoc}
	 * <p>
	 * Logs when data arrives on a channel. A typical listener would 
	 * examine the message to decide whether it should be discarded, 
	 * modified, or sent unchanged.
	 */
	public void receivedMessage(Channel channel, ClientSession session, ByteBuffer message){
		String decodedMessage = Serializer.decodeString(message);
		if (logger.isLoggable(Level.INFO)) {
			logger.log(Level.INFO,
					"Channel message {0} from {1} on channel {2}",
					new Object[] { decodedMessage, session.getName(), channel.getName() }
			);
		}
		if((decodedMessage!=null)&&(decodedMessage.startsWith("/"))){
			if(decodedMessage.startsWith("/leave")){
				session.send(Serializer.encodeString("You request to leave " + channel.getName() + " channel"));
				channel.leave(session);
			}
		}else{
			channel.send(session, Serializer.encodeString(decodedMessage));
		}
	}
}
