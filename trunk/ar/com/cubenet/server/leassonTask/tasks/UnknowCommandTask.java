package ar.com.cubenet.server.leassonTask.tasks;

import java.io.Serializable;

import ar.com.cubenet.common.leasson3.Serializer;

import com.sun.sgs.app.AppContext;
import com.sun.sgs.app.ClientSession;
import com.sun.sgs.app.ManagedReference;
import com.sun.sgs.app.Task;

public class UnknowCommandTask implements Task, Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1231182454061582998L;

	protected ManagedReference<ClientSession> sessionRef;
	
	protected String decodedMessage = null;
	
	public UnknowCommandTask(ClientSession session, String decodedMessage){
		this.sessionRef=AppContext.getDataManager().createReference(session);
		this.decodedMessage=decodedMessage;
	}
	
	@Override
	public void run() throws Exception {
		ClientSession session = sessionRef.get();
		session.send(Serializer.encodeString("Unknow command '" + decodedMessage + "'"));
	}
}
