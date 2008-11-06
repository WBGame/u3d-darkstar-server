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
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import ar.com.cubenet.common.leasson3.Serializer;

import com.sun.sgs.app.AppContext;
import com.sun.sgs.app.Channel;
import com.sun.sgs.app.ChannelManager;
import com.sun.sgs.app.ClientSession;
import com.sun.sgs.app.ClientSessionListener;
import com.sun.sgs.app.ManagedReference;

/**
 * Simple example {@link ClientSessionListener} for the Project Darkstar
 * Server.
 * <p>
 * Logs each time a session receives data or logs out, and echoes
 * any data received back to the sender.
 */
class ServerChannelsSessionListener implements Serializable, ClientSessionListener {

	/** The version of the serialized form of this class. */
	private static final long serialVersionUID = 1L;

	/** The {@link Logger} for this class. */
	private static final Logger logger = Logger.getLogger(ServerChannelsSessionListener.class.getName());

	/** The session this {@code ClientSessionListener} is listening to. */
	private final ManagedReference<ClientSession> sessionRef;
	/** 
	 * Lista para almacenar los clientes que se encuentran actualmente conectados. Supongo que
	 * debe (o debería) existir una manera mas elegante de obtener/almacenar los clientes actualmente
	 * conectados, pero no pude encontrarla.
	 * 
	 * @author Sebastián Perruolo
	 */
	private static Vector<String> clients = new Vector();
	/**
	 * Creates a new {@code HelloChannelsSessionListener} for the session.
	 *
	 * @param session the session this listener is associated with
	 * @param channel1 a reference to a channel to join
	 */
	public ServerChannelsSessionListener(ClientSession session,	ManagedReference<Channel> channel1)	{
		if (session == null){
			throw new NullPointerException("null session");
		}

		sessionRef = AppContext.getDataManager().createReference(session);

		// Join the session to all channels.  We obtain the channel
		// in two different ways, by reference and by name.
		ChannelManager channelMgr = AppContext.getChannelManager();

		// We were passed a reference to the first channel.
		channel1.get().join(session);
		// We look up the second channel by name.
		channelMgr.getChannel(ServerChannels.CHANNEL_2_NAME).join(session);;

		/*
		 * Se agrega el cliente a los dos channels que agregué.
		 * @author Sebastián Perruolo
		 */
		channelMgr.getChannel(ServerChannels.CHANNEL_3_NAME).join(session);
		
		channelMgr.getChannel(ServerChannels.CHANNEL_CLIENTS).join(session);
		
		clients.add(session.getName());
	}

	/**
	 * Returns the session for this listener.
	 * 
	 * @return the session for this listener
	 */
	protected ClientSession getSession() {
		// We created the ref with a non-null session, so no need to check it.
		return sessionRef.get();
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * Logs when data arrives from the client, and echoes the message back.
	 */
	public void receivedMessage(ByteBuffer message) {
		ClientSession session = getSession();
		String sessionName = session.getName();
		String decodedMessage = Serializer.decodeString(message);
		
		if (logger.isLoggable(Level.INFO)) {
			logger.log(Level.INFO, "Message {0} from {1}", new Object[] {decodedMessage, sessionName});
		}
		
        /* 
         * Se procesa el mensaje para determinar si es un comando o no.
         * @author Sebastián Perruolo
         */
		if(!decodedMessage.startsWith("/")){
			//si no es un comando..
			session.send(Serializer.encodeString(decodedMessage));
		}else{
			/*
			 * se procesan los comandos que son válidos para
			 * este tipo de conexión (directa con el server).
			 */
			if(decodedMessage.startsWith("/whoami")){
				session.send(Serializer.encodeString("You are '" + sessionName + "'"));
			}else if(decodedMessage.startsWith("/who")){
				StringBuffer who = new StringBuffer("|");
				for(String client : clients){
					who.append(client);
					who.append("|");
				}
				session.send(Serializer.encodeString(who.toString()));
			}else if(decodedMessage.startsWith("/login")){
				AppContext.getChannelManager().getChannel(ServerChannels.CHANNEL_CLIENTS)
						.send(session, Serializer.encodeString("User " + session.getName() + " has logged in"));
			}else{
				//se recibió un comando pero no se sabe interpretar!
				session.send(Serializer.encodeString("Unknow command '" + decodedMessage + "'"));
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * Logs when the client disconnects.
	 */
	public void disconnected(boolean graceful) {
		ClientSession session = getSession();
		String grace = graceful ? "graceful" : "forced";
		logger.log(Level.INFO, "User {0} has logged out {1}", new Object[] { session.getName(), grace } );
		clients.remove(session.getName());
	}
}
