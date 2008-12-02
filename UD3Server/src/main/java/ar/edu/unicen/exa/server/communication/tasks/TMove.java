/**
 * 
 */
package server.communication.tasks;

import common.messages.IMessage;
import common.messages.notify.MsgMove;

/**
 * Tarea relacionada al mensaje de movimiento {@link MsgMove}.<BR/>
 * Devera actualizar la posicion de la entidad en movimiento, y realizar toda la
 * logica asociada a la subscripcion de celdas y el reenvio del mensaje a travez
 * de ellas.
 * 
 * @author lito
 */
public class TMove extends TaskCommunication {
	
	/**
	 * @param msg
	 */
	public TMove(IMessage msg) {
		super(msg);
	}
	
	/**
	 * TODO hacer javaDoc
	 * 
	 * @param msg
	 * @return
	 */
	@Override
	public TaskCommunication factoryMethod(IMessage msg) {
		return new TMove(msg);
	}
	
	public void run() {
		
	}
	
}
