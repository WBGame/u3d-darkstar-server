package ar.edu.unicen.exa.server.communication.tasks;

import common.messages.IMessage;

/**
 * Tarea relacionada al mensaje de movimiento {@link MsgChangePlayerState}.<BR/>
 * Deberá actualizar el estado de la entidad afectada, y reenviar
 * el mensaje a través las celdas pertinentes.
 * 
 */
public final class TChangePlayerState extends TaskCommunication {
	
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
	public TaskCommunication factoryMethod(final IMessage msg) {
		return new TChangePlayerState(msg);
	}
	

	/**
	 * TODO comentar.
	 */
	public void run() {
		/*
		 * TODO implementar. El mensaje que se recibirá: MsgChangePlayerState
		 */
	}
	
}
