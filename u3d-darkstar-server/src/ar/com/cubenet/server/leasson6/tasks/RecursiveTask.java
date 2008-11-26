package ar.com.cubenet.server.leasson6.tasks;

import java.io.Serializable;

import ar.com.cubenet.common.leasson3.Serializer;
import ar.com.cubenet.server.leasson6.TaskAppListener;

import com.sun.sgs.app.AppContext;
import com.sun.sgs.app.ClientSession;
import com.sun.sgs.app.ManagedReference;
import com.sun.sgs.app.Task;
import com.sun.sgs.app.TaskManager;

/**
 * This Task create and enqueue a similar task until it
 * reach a MAX of five instances created.
 * 
 * @author Sebastián Perruolo
 *
 */
public class RecursiveTask implements Task, Serializable {

	/** The version of the serialized form of this class. */
	private static final long serialVersionUID = 9064186648286012726L;

	/** Max number of iterations. */
	private static final int MAX = 5;
	/**
	 * Referencia a la sesión del cliente que acaba de loguearse.
	 */
	private ManagedReference<ClientSession> sessionRef;
	
	/** The name of this instance. */
	private int count;
	
	/** The message to send. */
	private String message;

	/**
	 * Creador.
	 * @param session sesión del cliente que acaba de loguearse.
	 * @param iteration count the number of instances created
	 * @param sendMessage message to send
	 */
	public RecursiveTask(final ClientSession session, final int iteration, 
				final String sendMessage) {
		this.sessionRef = AppContext.getDataManager().createReference(session);
		this.count = iteration;
		this.message = sendMessage;
	}
	/**
	 * Create a RecursiveTask and enqueue.
	 * 
	 * @throws Exception if the action fails.
	 */
	@Override
	public final void run() throws Exception {
		ClientSession session = sessionRef.get();
		TaskManager taskManager = AppContext.getTaskManager();
		
		if (count < MAX) {
			RecursiveTask recursiveTask = new RecursiveTask(
					session, count + 1, message
				);
			taskManager.scheduleTask(recursiveTask);
		}
		
		AppContext.getChannelManager()
		.getChannel(TaskAppListener.TASK_CHANNEL_NAME)
			.send(
				session, 
				Serializer.encodeString(
						" recursive task " + count + " send message: " 
						+ message
				)
			);
	}

}
