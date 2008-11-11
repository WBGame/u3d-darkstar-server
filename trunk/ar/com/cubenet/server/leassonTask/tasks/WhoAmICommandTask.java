package ar.com.cubenet.server.leassonTask.tasks;

import java.io.Serializable;

import ar.com.cubenet.common.leasson3.Serializer;

import com.sun.sgs.app.AppContext;
import com.sun.sgs.app.ClientSession;
import com.sun.sgs.app.ManagedReference;
import com.sun.sgs.app.Task;

public class WhoAmICommandTask implements Task, Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8170699850739068873L;
	
	protected ManagedReference<ClientSession> sessionRef;
	
	public WhoAmICommandTask(ClientSession session){
		this.sessionRef=AppContext.getDataManager().createReference(session); 
	}
	
	@Override
	public void run() throws Exception {
		ClientSession session = sessionRef.get();
		session.send(Serializer.encodeString("You are '" + session.getName() + "'"));
	}

}
