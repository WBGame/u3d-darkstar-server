package ar.com.cubenet.server.leasson6.tasks;

import java.io.Serializable;

import ar.com.cubenet.common.leasson3.Serializer;
import ar.com.cubenet.server.leasson6.TaskAppListener;

import com.sun.sgs.app.AppContext;
import com.sun.sgs.app.ClientSession;
import com.sun.sgs.app.ManagedReference;
import com.sun.sgs.app.Task;

/**
 * A simple task, just send message through the Channel.
 * @author Sebastián Perruolo
 *
 */
public class SimpleTask implements Task, Serializable {

	/** The version of the serialized form of this class. */
	private static final long serialVersionUID = -8727244524575615473L;

	/**
	 * Referencia a la sesión del cliente que acaba de loguearse.
	 */
	private ManagedReference<ClientSession> sessionRef;
	
	/** The name of this instance. */
	private String title;
	
	/** The message to send. */
	private String message;
	
	/**
	 * Creador.
	 * @param session sesión del cliente que acaba de loguearse.
	 * @param taskTitle name of this SimpleTasks
	 * @param sendMessage message to send
	 */
	public SimpleTask(final ClientSession session, final String taskTitle, 
				final String sendMessage) {
		this.sessionRef = AppContext.getDataManager().createReference(session);
		this.title = taskTitle;
		this.message = sendMessage;
	}

	/**
	 * Just send message through the Channel.
	 * @throws Exception if the action fails.
	 */
	@Override
	public final void run() throws Exception {
		ClientSession session = sessionRef.get();
		
		AppContext.getChannelManager()
				.getChannel(TaskAppListener.TASK_CHANNEL_NAME)
				.send(
						session, 
						Serializer.encodeString(
								title
								+ " send message: " 
								+ message
						)
				);
	}

}
