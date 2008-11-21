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

package ar.com.cubenet.server.leassonTask;

import java.io.Serializable;
import java.nio.ByteBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;

import ar.com.cubenet.common.leasson3.Serializer;

import com.sun.sgs.app.Channel;
import com.sun.sgs.app.ChannelListener;
import com.sun.sgs.app.ClientSession;

/**
 * Este channel no reenvía a los demás clientes los mensajes recibidos.
 * Sólo se utiliza para avisar a los clientes que un nuevo cliente se 
 * ha logueado
 * @author Sebastián Perruolo
 */
class ServerChannelsNullListener implements Serializable, ChannelListener {
	/** The version of the serialized form of this class. */
	private static final long serialVersionUID = 1L;

	/** The {@link Logger} for this class. */
	private static final Logger LOGGER = Logger.getLogger(
			ServerChannelsNullListener.class.getName()
	);

	/**
	 * Deshecha los mensajes que llegan.
	 * 
	 * @param channel no importa, no se usa
	 * @param session no importa, no se usa
	 * @param message no importa, no se usa
	 */
	public void receivedMessage(
			final Channel channel, 
			final ClientSession session, 
			final ByteBuffer message
	) {
		String decodedMessage = Serializer.decodeString(message);
		//no hago nada, solo logueo
		if (LOGGER.isLoggable(Level.INFO)) {
			LOGGER.log(Level.INFO,
					"Channel message {0} from {1} on {2} disposed",
					new Object[] { 
						decodedMessage, 
						session.getName(), 
						channel.getName() 
					}
			);
		}
	}
}
