package ar.edu.unicen.exa.server.communication.tasks;

import common.messages.IMessage;

/**
 * La tarea se ejecutará al recibir un mensaje ({@link MsgGetDynamicEntity})
 * directo desde un cliente, que desea la infomación completa de una entidad
 * dinámica cuyo identificador esta presente en el mensaje. <br/> 
 * Los pasos a seguir son: <br/>
 * * Obtener la entidad dinámica a partir del DataManager. <br/>
 * * Construir un mensaje del tipo {@link MsgGetDynamicEntityResponse} y
 * setearle la entidad dynamica. <br/>
 * * Enviarle el mensaje construido de vuelta al jugador que solicitó la
 * información.
 * 
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
