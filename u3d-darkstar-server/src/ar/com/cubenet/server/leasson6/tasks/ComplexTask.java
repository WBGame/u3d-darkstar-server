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
 * This Task creates child tasks and enqueue them. Darkstar ensure that
 * this task will be executed before all child tasks it creates.
 * 
 * Note: I really do not know how to create child task, so I decided
 * to create it inside run method.
 * 
 * @author Sebastián Perruolo
 *
 */
public class ComplexTask implements Task, Serializable {

	/** The version of the serialized form of this class. */
	private static final long serialVersionUID = -7326115383222083348L;

	/**
	 * Referencia a la sesión del cliente que acaba de loguearse.
	 */
	private ManagedReference<ClientSession> sessionRef;
	
	/** The message to send. */
	private String message;

	/**
	 * Creador.
	 * @param session sesión del cliente que acaba de loguearse.
	 * @param sendMessage message to send
	 */
	public ComplexTask(final ClientSession session, final String sendMessage) {
		this.sessionRef = AppContext.getDataManager().createReference(session);
		this.message = sendMessage;
	}
	
	/**
     * Performs an action, throwing an exception if the action fails.
     * Child tasks are created and enqueued.
     *
     * @throws	Exception if the action fails
	 */
	@Override
	public final void run() throws Exception {
		ClientSession session = sessionRef.get();
		TaskManager taskManager = AppContext.getTaskManager();
		SimpleTask simpleTasks1 = new SimpleTask(session, "simple1", message);
		taskManager.scheduleTask(simpleTasks1);
		
		SimpleTask simpleTasks2 = new SimpleTask(session, "simple2", message);
		taskManager.scheduleTask(simpleTasks2);
		
		SimpleTask simpleTasks3 = new SimpleTask(session, "simple3", message);
		taskManager.scheduleTask(simpleTasks3);
		
		
		taskManager.scheduleTask(new RecursiveTask(session, 0, message));

		AppContext.getChannelManager()
		.getChannel(TaskAppListener.TASK_CHANNEL_NAME)
		.send(
				session, 
				Serializer.encodeString(
						"complex task send message: " 
						+ message
				)
		);
	}

}
