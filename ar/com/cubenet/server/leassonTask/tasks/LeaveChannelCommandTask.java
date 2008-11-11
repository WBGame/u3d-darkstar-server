package ar.com.cubenet.server.leassonTask.tasks;

import java.io.Serializable;

import ar.com.cubenet.common.leasson3.Serializer;

import com.sun.sgs.app.AppContext;
import com.sun.sgs.app.Channel;
import com.sun.sgs.app.ClientSession;
import com.sun.sgs.app.ManagedReference;
import com.sun.sgs.app.Task;

/**
 * Comando (Task) de ejemplo que remueve un usuario del canal indicado.
 * @author Sebastián Perruolo
 *
 */
public class LeaveChannelCommandTask implements Task, Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8157318761331054687L;
	/**
	 * Si queremos (como en este caso) manejar una referencia a un 
	 * ManagedObject (ClientSession es un ManagedObject) deberemos
	 * hacerlo a través de una ManagedReference.
	 * @author Sebastián Perruolo 
	 */
	protected ManagedReference<ClientSession> sessionRef;
	protected String channelName = null;
	
	public LeaveChannelCommandTask(ClientSession session, String channelName){
		this.sessionRef=AppContext.getDataManager().createReference(session);
		this.channelName = channelName;
	}
	
	@Override
	public void run() throws Exception {
		// .get() nos permite acceder al ClientSession
		ClientSession session = sessionRef.get();
		Channel channel = AppContext.getChannelManager().getChannel(channelName);
		session.send(Serializer.encodeString("You request to leave " + channel.getName() + " channel"));
		channel.leave(session);
	}
}
