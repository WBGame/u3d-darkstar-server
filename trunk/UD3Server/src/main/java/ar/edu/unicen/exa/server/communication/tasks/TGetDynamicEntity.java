package ar.edu.unicen.exa.server.communication.tasks;

import common.messages.IMessage;

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
public final class TGetDynamicEntity extends TaskCommunication {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8571194487175360055L;
	/**
	 * The Constructor.
	 * 
	 * @param msg the msg
	 */
	public TGetDynamicEntity(final IMessage msg) {
		super(msg);
	}
	
	/**
	 * TODO hacer javaDoc.
	 * 
	 * @param msg the msg
	 * 
	 * @return the task communication
	 */
	@Override
	public TaskCommunication factoryMethod(final IMessage msg) {
		return new TGetDynamicEntity(msg);
	}
	/**
	 * TODO.
	 */
	public void run() {
		
	}
	
}
