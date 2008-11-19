package ar.com.cubenet.server.leassonTask.tasks;

import java.io.Serializable;

import ar.com.cubenet.common.leasson3.Serializer;
import ar.com.cubenet.server.leassonTask.ServerChannels;

import com.sun.sgs.app.AppContext;
import com.sun.sgs.app.ClientSession;
import com.sun.sgs.app.ManagedReference;
import com.sun.sgs.app.Task;

/**
 * Tarea que realiza un broadcasting para avisar a todos los clientes
 * que otro cliente acaba de loguearse.
 */
public class BroadcastLoginCommandTask implements Task, Serializable {

	/**
	 * The version of the serialized form of this class.
	 */
	private static final long serialVersionUID = 4385502356171294428L;

	/**
	 * Referencia a la sesión del cliente que acaba de loguearse.
	 */
	private ManagedReference<ClientSession> sessionRef;
	
	/**
	 * Creador.
	 * @param session sesión del cliente que acaba de loguearse.
	 */
	public BroadcastLoginCommandTask(final ClientSession session) {
		this.sessionRef = AppContext.getDataManager().createReference(session); 
	}
	
	/**
	 * Este método ejecuta la acción encapsulada en este objeto:
	 * Se obtiene el channel utilizado para enviar los avisos
	 * de logueo y se envía el mensaje avisando que el nuevo
	 * cliente acaba de loguearse.
	 * 
	 * @throws Exception si la acción falla.
	 * @see Task
	 */
	public final void run() throws Exception {
		ClientSession session = sessionRef.get();
		AppContext.getChannelManager()
				.getChannel(ServerChannels.CHANNEL_CLIENTS)
				.send(
					session, 
					Serializer.encodeString(
							"User " 
							+ session.getName() 
							+ " has logged in"
					)
				);
	}
}
