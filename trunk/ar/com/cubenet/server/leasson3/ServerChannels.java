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
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.sun.sgs.app.AppContext;
import com.sun.sgs.app.AppListener;
import com.sun.sgs.app.Channel;
import com.sun.sgs.app.ChannelManager;
import com.sun.sgs.app.ClientSession;
import com.sun.sgs.app.ClientSessionListener;
import com.sun.sgs.app.Delivery;
import com.sun.sgs.app.ManagedObject;
import com.sun.sgs.app.ManagedReference;

/**
 * Simple example of channel operations in the Project Darkstar Server.
 * <p>
 * Extends the {@code HelloEcho} example by joining clients to two
 * channels.
 * 
 **********
 * @author Sebastián Perruolo:
 * Este ejemplo fue modificado para hacer algunas cosas inútiles pero
 * didácticas. 
 */
public class ServerChannels implements Serializable, AppListener, ManagedObject {

    /** The version of the serialized form of this class. */
    private static final long serialVersionUID = 1L;

    /** The {@link Logger} for this class. */
    private static final Logger logger = Logger.getLogger(ServerChannels.class.getName());

    /* The name of the first channel {@value #CHANNEL_1_NAME} */
    static final String CHANNEL_1_NAME = "Foo";
    /* The name of the second channel {@value #CHANNEL_2_NAME} */
    static final String CHANNEL_2_NAME = "Bar";

    /**
     * El nombre del channel <i>broadcasteador</i>. Ver los links en "See Also" para 
     * entender donde se crean los channels y donde se relacionan con el cliente.
     *  
     * @see ServerChannels#initialize(Properties)
     * @see ServerChannelsSessionListener#ServerChannelsSessionListener(ClientSession, ManagedReference)
     * @author Sebastián Perruolo  
     */
    static final String CHANNEL_3_NAME = "U3D";

    /** 
     * El nombre del channel que utilizaremos para enviar información
     * a los clientes. En un principio para avisar quien se logueó
     * @author Sebastián Perruolo
     */
    static final String CHANNEL_CLIENTS = "clients";
    
    /** 
     * The first {@link Channel}.  The second channel is looked up
     * by name.
     */
    private ManagedReference<Channel> channel1 = null;
    

    /**
     * {@inheritDoc}
     * <p>
     * Creates the channels.  Channels persist across server restarts,
     * so they only need to be created here in {@code initialize}.
     */
    public void initialize(Properties props) {
    	
        ChannelManager channelMgr = AppContext.getChannelManager();
        
        // Create and keep a reference to the first channel.
        Channel c1 = channelMgr.createChannel(CHANNEL_1_NAME, 
                                              null,                              //null porque alcanza con el comportamiento común
                                              Delivery.RELIABLE);
        channel1 = AppContext.getDataManager().createReference(c1);
        
        // We don't keep a reference to the second channel, to demonstrate
        // looking it up by name when needed.  Also, this channel uses a
        // {@link ChannelListener} to filter messages.
        Channel c2 = channelMgr.createChannel(CHANNEL_2_NAME, 
                                 new ServerChannelsChannelListener(),			//comportamiento básico 
                                 Delivery.RELIABLE);

        /* 
         * Se crean los siguientes dos channels.
         * @author Sebastián Perruolo
         */
        channelMgr.createChannel(CHANNEL_3_NAME, 
        		new ServerChannelsBroadcastListener(CHANNEL_2_NAME),			//comportamiento de broadcasting
                Delivery.RELIABLE);
        
        channelMgr.createChannel(CHANNEL_CLIENTS, 
        		new ServerChannelsNullListener(),								//deshecho los mensajes
                Delivery.RELIABLE);
        
    }

    /**
     * {@inheritDoc}
     * <p>
     * Returns a {@link ServerChannelsSessionListener} for the
     * logged-in session.
     */
    public ClientSessionListener loggedIn(ClientSession session) {
        logger.log(Level.INFO, "User {0} has logged in", session.getName());
        return new ServerChannelsSessionListener(session, channel1);
    }

}
