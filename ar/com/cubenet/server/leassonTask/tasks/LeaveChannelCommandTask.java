package ar.com.cubenet.server.leassonTask.tasks;

import java.io.Serializable;

import ar.com.cubenet.common.leasson3.Serializer;

import com.sun.sgs.app.AppContext;
import com.sun.sgs.app.Channel;
import com.sun.sgs.app.ClientSession;
import com.sun.sgs.app.ManagedReference;
import com.sun.sgs.app.Task;

/**
 * Comando (Task) de ejemplo que remueve un usuario del canal indicado.
 * @author Sebastián Perruolo
 *
 */
public class LeaveChannelCommandTask implements Task, Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8157318761331054687L;
	
	/**
	 * Si queremos (como en este caso) manejar una referencia a un 
	 * ManagedObject (ClientSession es un ManagedObject) deberemos
	 * hacerlo a través de una ManagedReference.
	 *  
	 */
	private ManagedReference<ClientSession> sessionRef;
	
	/**
	 * 
	 */
	private String channelName = null;
	
	/**
	 * 
	 * @param session     session del cliente
	 * @param channel nombre del canal
	 */
	public LeaveChannelCommandTask(
			final ClientSession session, 
			final String channel
	) {
		this.setSessionRef(session);
		this.setChannelName(channel); 
	}

	/**
	 * FALTA DOC.
	 * 
	 * @throws Exception descripción.
	 * @see interface Task.
	 */	
	public final void run() throws Exception {
		// .get() nos permite acceder al ClientSession
		ClientSession session = sessionRef.get();
		Channel channel = AppContext.getChannelManager().getChannel(
				channelName
		);
		session.send(
				Serializer.encodeString(
						"You request to leave " 
						+ channel.getName() + " channel"
				)
		);
		
		channel.leave(session);
	}

	/**
	 * 
	 * @return ClientSession
	 */
	public final ClientSession getSessionRef() {
		return sessionRef.get();
	}

	/**
	 * Setter.
	 * 
	 * @param session instancia de la session del cliente.
	 */
	public final void setSessionRef(final ClientSession session) {
		this.sessionRef = AppContext.getDataManager().createReference(
				session
		);
	}

	/**
	 * Getter.
	 * 
	 * @return String
	 */
	public final String getChannelName() {
		return channelName;
	}

	/**
	 * Setter.
	 * 
	 * @param name nombre del channel.
	 */
	public final void setChannelName(final String name) {
		this.channelName = name;
	}
}
