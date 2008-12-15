/**
 * 
 */
package ar.edu.unicen.exa.server.communication.tasks;

import common.messages.IMessage;

/**
 * Tarea relacionada al mensaje de movimiento {@link MsgChangePlayerState}.<BR/>
 * Devera actualizar el estado de la entidad afectada, y reenviar
 * el mensaje a travez las celdas pertinentes.
 * 
 * @author lito
 */
public class TChangePlayerState extends TaskCommunication {
	
	/**
	 * The Constructor.
	 * 
	 * @param msg the msg
	 */
	public TChangePlayerState(final IMessage msg) {
		super(msg);
	}
	
	/**
	 * Create the Task across the factory.
	 * 
	 * @param msg the msg
	 * 
	 * @return the task communication
	 */
	@Override
	public final TaskCommunication factoryMethod(final IMessage msg) {
		return new TChangePlayerState(msg);
	}
	

	/**
	 * 
	 */
	public void run() {
		
	}
	
}
