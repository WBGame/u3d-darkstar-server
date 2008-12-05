/**
 * 
 */
package ar.edu.unicen.exa.server.communication.tasks;

import common.messages.IMessage;

/**
 * La tarea se ejecutara al recibir un mensaje directo desde un cliente, el cual
 * informa que entrara al mundo indicado en el mensaje. Las acciones a tomar son
 * las siguiente:<BR/>
 * * Agragar el jugador al mundo desedeado.<BR/>
 * * Suscribirlo a la celda por defecto del mundo y a las adyacentes segun
 * coresponda.<BR/>
 * * Quitarlo del mundo en el que estaba el jugador previamente.<BR/>
 * * Desuscribirlo de las celdas en las que se encontraba en el mundo anterior.<BR/>
 * * Enviar los mensaje correspondientes {@link MsgArrived} a las celdas del mundo
 * nuevo.<BR/>
 * * Enviar los mensaje correspondientes {@link MsgLeft} a las celdas del mundo
 * viejo.<BR/>
 * 
 * @author lito
 * 
 */
public class TEnterWorld extends TaskCommunication {
	
	/**
	 * @param msg
	 */
	public TEnterWorld(IMessage msg) {
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
		return new TEnterWorld(msg);
	}
	
	public void run() {
		
	}
	
}
