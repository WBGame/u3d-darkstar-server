package ar.com.cubenet.server.leassonTask.command;

import ar.com.cubenet.server.leassonTask.tasks.BroadcastLoginCommandTask;
import ar.com.cubenet.server.leassonTask.tasks.LeaveChannelCommandTask;
import ar.com.cubenet.server.leassonTask.tasks.UnknowCommandTask;
import ar.com.cubenet.server.leassonTask.tasks.WhoAmICommandTask;

import com.sun.sgs.app.AppContext;
import com.sun.sgs.app.ClientSession;
import com.sun.sgs.app.Task;

public class CommandManager {
	
	protected static CommandManager instance = null;
	
	protected CommandManager() {
		
	}
	
	public static CommandManager getInstance() {
		if (instance == null) {
			instance = new CommandManager();
		}
		return instance;
	}
	
	/**
	 * FALTA DOC.
	 * 
	 * @param msj
	 * @param session
	 * @param channelName
	 */
	public void process(String msj, ClientSession session, String channelName) {
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
		
		if (task != null) 
			AppContext.getTaskManager().scheduleTask(task);
	}
}
