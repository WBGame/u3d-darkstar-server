package ar.com.cubenet.server.leassonTask.tasks;

import java.io.Serializable;

import ar.com.cubenet.common.leasson3.Serializer;

import com.sun.sgs.app.AppContext;
import com.sun.sgs.app.ClientSession;
import com.sun.sgs.app.ManagedReference;
import com.sun.sgs.app.Task;
/**
 * Esta clase encapsula un comando que le permite al usuario
 * saber quien es... (completamente llena de utilidad, ¿no?).
 * También se podría llamar MirrorCommandTask, pero para eso 
 * tendría que dar vuelta al usuario: 'tneduts a' era ouY. (es tarde..) 
 * 
 * @author Sebastián Perruolo
 */
public class WhoAmICommandTask implements Task, Serializable {
	/**
	 * The version of the serialized form of this class.
	 */
	private static final long serialVersionUID = 8170699850739068873L;
	
	/**
	 * Guarda la sesión del cliente que quiere saber quien es.
	 */
	private ManagedReference<ClientSession> sessionRef;
	
	/**
	 * Creador.
	 * @param session del cliente que quiere saber quien es.
	 */
	public WhoAmICommandTask(final ClientSession session) {
		this.sessionRef = AppContext.getDataManager().createReference(session); 
	}
	
	/**
	 * Este método ejecuta la acción encapsulada en este objeto:
	 * Sólo envía un texto para decirle al cliente su nombre de cliente.
	 * 
	 * @throws Exception si la acción falla.
	 * @see Task
	 */
	public final void run() throws Exception {
		ClientSession session = sessionRef.get();
		session.send(
				Serializer.encodeString("You are '" + session.getName() + "'"));
	}

}
