/**
 * 
 */
package ar.edu.unicen.exa.server.communication.tasks;

import common.messages.IMessage;
import common.messages.notify.MsgRotate;

/**
 * Tarea relacionada al mensaje de movimiento {@link MsgRotate}.<BR/>
 * Devera actualizar el angulo de rotacion de la entidad afectada, y reenviar
 * el mensaje a travez las celdas pertinentes.
 * 
 * @author lito
 */
public class TRotate extends TaskCommunication {
	
	/**
	 * @param msg
	 */
	public TRotate(IMessage msg) {
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
		return new TRotate(msg);
	}
	
	public void run() {
		
	}
	
}
