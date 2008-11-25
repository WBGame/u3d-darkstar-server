package ar.com.cubenet.server.leasson4.command;

import ar.com.cubenet.server.leasson4.tasks.BroadcastLoginCommandTask;
import ar.com.cubenet.server.leasson4.tasks.LeaveChannelCommandTask;
import ar.com.cubenet.server.leasson4.tasks.UnknowCommandTask;
import ar.com.cubenet.server.leasson4.tasks.WhoAmICommandTask;

import com.sun.sgs.app.AppContext;
import com.sun.sgs.app.ClientSession;
import com.sun.sgs.app.Task;

/**
 * Esta clase permite manejar los comandos que llegan al
 * servidor.
 * 
 * @author Sebastián Perruolo
 */
public class CommandManager {
	
	/**
	 * Instancia de CommandManager.
	 */
	private static CommandManager instance = null;
	
	/**
	 * Este método es protected para implementar un Singleton.
	 */
	protected CommandManager() {
		
	}
	/**
	 * Método que permite acceder a una instancia de
	 * esta clase.
	 * @return una instancia única de CommandManager.
	 */
	public static CommandManager getInstance() {
		if (instance == null) {
			instance = new CommandManager();
		}
		return instance;
	}
	
	/**
	 * Este método procesa el comando recibido. Se crea una
	 * instancia de la tarea adecuada y se encola en el 
	 * TaskManager.
	 * 
	 * @param msj Comando a procesar
	 * @param session Cliente que envió el mensaje
	 * @param channelName Nombre del channel que recibió el mensaje
	 */
	public final void process(final String msj, 
			final ClientSession session, final String channelName) {
		Task task = null;
		if (msj.equals("/whoami")) {
			task = new WhoAmICommandTask(session);
		} else 
			if (msj.equals("/login")) {
				task = new BroadcastLoginCommandTask(session);
			} else 
				if (msj.equals("/leave")) {
					task = new LeaveChannelCommandTask(session, channelName);
				} else {
					task = new UnknowCommandTask(session, msj);
				}
		
		if (task != null) {
			AppContext.getTaskManager().scheduleTask(task);
		}

	}
}
