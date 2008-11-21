package ar.com.cubenet.server.leassonTask.tasks;

import java.io.Serializable;

import ar.com.cubenet.common.leasson3.Serializer;

import com.sun.sgs.app.AppContext;
import com.sun.sgs.app.Channel;
import com.sun.sgs.app.ClientSession;
import com.sun.sgs.app.ManagedReference;
import com.sun.sgs.app.Task;

/**
 * Comando (Task) de ejemplo que remueve un cliente del canal indicado.
 * @author Sebastián Perruolo
 *
 */
public class LeaveChannelCommandTask implements Task, Serializable {
	
	/**
	 * The version of the serialized form of this class.
	 */
	private static final long serialVersionUID = -8157318761331054687L;
	
	/**
	 * Si queremos (como en este caso) manejar una referencia a un 
	 * ManagedObject (ClientSession es un ManagedObject) deberemos
	 * hacerlo a través de una ManagedReference.
	 * 
	 * sessionRef guarda la sesión del cliente que quiere abandonar
	 * el channel.
	 */
	private ManagedReference<ClientSession> sessionRef;
	
	/**
	 * Nombre del channel del que se debe remover el cliente.
	 */
	private String channelName = null;
	
	/**
	 * Creador.
	 * 
	 * @param session session del cliente que quiere abandonar el channel.
	 * @param channel nombre del channel a abandonar.
	 */
	public LeaveChannelCommandTask(final ClientSession session, 
			final String channel) {
		this.setSessionRef(session);
		this.setChannelName(channel); 
	}

	/**
	 * Este método ejecuta la acción encapsulada en este objeto:
	 * Se obtiene la referencia del channel que quiere abandonar
	 * el cliente (recordad que hasta ahora sólo tenía guardado
	 * el nombre del channel). Luego se le envía un mensaje al
	 * cliente diciendole que pidió abandonar el channel.
	 * Luego se remueve el cliente del channel, lo que hace
	 * que se ejecute el método leftChannel() del listener
	 * que tiene el cliente.
	 * 
	 * @throws Exception si la acción falla.
	 * @see Task
	 */	
	public final void run() throws Exception {
		// .get() nos permite acceder al ClientSession
		ClientSession session = sessionRef.get();
		Channel channel = AppContext.getChannelManager()
				.getChannel(channelName);
		session.send(
				Serializer.encodeString(
						"You request to leave " 
						+ channel.getName() + " channel"
				)
		);
		
		channel.leave(session);
	}

	/**
	 * Setea la referencia del cliente.
	 * 
	 * @param session instancia de la session del cliente.
	 */
	public final void setSessionRef(final ClientSession session) {
		this.sessionRef = AppContext.getDataManager()
				.createReference(session);
	}

	/**
	 * Setea el nombre del channel que quiere abandonar el cliente.
	 * 
	 * @param name nombre del channel.
	 */
	public final void setChannelName(final String name) {
		this.channelName = name;
	}
}
