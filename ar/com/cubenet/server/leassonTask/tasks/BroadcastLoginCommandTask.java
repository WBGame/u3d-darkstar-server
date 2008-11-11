package ar.com.cubenet.server.leassonTask.tasks;

import java.io.Serializable;

import ar.com.cubenet.common.leasson3.Serializer;
import ar.com.cubenet.server.leassonTask.ServerChannels;

import com.sun.sgs.app.AppContext;
import com.sun.sgs.app.ClientSession;
import com.sun.sgs.app.ManagedReference;
import com.sun.sgs.app.Task;

public class BroadcastLoginCommandTask implements Task, Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4385502356171294428L;
	
	protected ManagedReference<ClientSession> sessionRef;
	
	public BroadcastLoginCommandTask(ClientSession session){
		this.sessionRef=AppContext.getDataManager().createReference(session); 
	}
	@Override
	public void run() throws Exception {
		ClientSession session = sessionRef.get();
		AppContext.getChannelManager()
				.getChannel(ServerChannels.CHANNEL_CLIENTS)
				.send(
					session, 
					Serializer.encodeString("User " + session.getName() + " has logged in"));
	}

}
