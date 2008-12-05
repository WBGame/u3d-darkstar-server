/**
 * 
 */
package ar.edu.unicen.exa.server.communication.tasks;

import common.messages.IMessage;
import common.messages.responses.MsgGetDynamicEntityResponse;

/**
 * La tarea se ejecutara al recibir un mensaje ({@link MsgGetDynamicEntity})
 * directo desde un cliente, que desea la infomacion completa de una entidad
 * dinamica cuyo identificador esta precente en el mensaje. Los pasos a seguir
 * son:<BR/>
 * * Obtener la entidad dinamica a partir del DataManager.<BR/>
 * * Construir un mensaje del tipo {@link MsgGetDynamicEntityResponse} y
 * setearle la entidad dynamica.<BR/>
 * * Enviarle el mensaje construido devuelta al jugador que solicito la
 * informacion.
 * 
 * @author lito
 */
public class TGetDynamicEntity extends TaskCommunication {
	
	/**
	 * @param msg
	 */
	public TGetDynamicEntity(IMessage msg) {
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
		return new TGetDynamicEntity(msg);
	}
	
	public void run() {
		
	}
	
}
