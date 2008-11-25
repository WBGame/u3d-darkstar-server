package ar.com.cubenet.server.leasson4.tasks;

import java.io.Serializable;

import ar.com.cubenet.common.leasson3.Serializer;

import com.sun.sgs.app.AppContext;
import com.sun.sgs.app.ClientSession;
import com.sun.sgs.app.ManagedReference;
import com.sun.sgs.app.Task;
/**
 * Esta clase se creó menos por utilidad que por fines de
 * ejemplo. Solamente le avisa al usuario que acaba de
 * enviar un comando desconocido.
 * 
 * @author Sebastián Perruolo
 */
public class UnknowCommandTask implements Task, Serializable {

	/**
	 * The version of the serialized form of this class.
	 */
	private static final long serialVersionUID = -1231182454061582998L;

	/**
	 * Guarda la sesión del cliente que ejecutó el comando desconocido.
	 */
	private ManagedReference<ClientSession> sessionRef;
	
	/**
	 * Mensaje (en realidad: comando) que envió el cliente.
	 */
	private String command = null;
	
	/**
	 * Creador.
	 * 
	 * @param session Sesión del cliente que ejecutó <s>cualquier
	 * cosa</s> un comando desconocido.
	 *  
	 * @param unknowCommand Comando desconocido
	 */
	public UnknowCommandTask(final ClientSession session, 
				final String unknowCommand) {
		this.sessionRef = AppContext.getDataManager().createReference(session);
		this.command = unknowCommand;
	}
	/**
	 * Este método ejecuta la acción encapsulada en este objeto:
	 * Sólo envía un texto para avisarle al cliente que ejecutó un comando
	 * desconocido.
	 * 
	 * @throws Exception si la acción falla.
	 * @see Task
	 */
	public final void run() throws Exception {
		ClientSession session = sessionRef.get();
		session.send(
				Serializer.encodeString("Unknow command '" + command + "'"));
	}
}
