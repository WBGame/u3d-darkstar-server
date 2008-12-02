/**
 * 
 */
package server.communication.tasks;

import common.messages.IMessage;
import common.messages.notify.MsgChangePlayerState;

/**
 * Tarea relacionada al mensaje de movimiento {@link MsgChangePlayerState}.<BR/>
 * Devera actualizar el estado de la entidad afectada, y reenviar
 * el mensaje a travez las celdas pertinentes.
 * 
 * @author lito
 */
public class TChangePlayerState extends TaskCommunication {
	
	/**
	 * @param msg
	 */
	public TChangePlayerState(IMessage msg) {
		super(msg);
	}
	
	/**
	 * TODO hacer javaDoc
	 * @param msg
	 * @return
	 */
	@Override
	public TaskCommunication factoryMethod(IMessage msg) {
		return new TChangePlayerState(msg);
	}
	

	public void run() {
		
	}
	
}
