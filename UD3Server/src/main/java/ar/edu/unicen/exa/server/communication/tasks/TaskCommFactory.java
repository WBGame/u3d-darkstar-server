/**
 * ComTaskFactory.java
 * 
 * @author lito
 */
package ar.edu.unicen.exa.server.communication.tasks;

import java.util.Hashtable;

import common.exceptions.UnsopportedMessageException;
import common.messages.IMessage;
import common.messages.MessageFactory;
import common.messages.MsgTypes;

/**
 * Fábrica de tareas de comunicación. Para utilizarla, primero se debe obtener
 * su única instancia a travez del método {@link #getInstance()} (ya que sigue
 * el patrón <i>singleton</i>). Esta clase permite crear e inicializar una
 * instacia de la tarea apropiada para un mensaje dado. Las asociaciones entre
 * tipos de mensajes y tareas correspondientes se mantienen en un hash interno,
 * para el que se brindan métodos de manipulación.
 * 
 * @author Diego
 */
public class TaskCommFactory {
	
	/**
	 * Contiene el mapeo de los tipos de mensajes con la tarea encargada de
	 * realizar la acción correspondiente.
	 */
	private Hashtable<String, TaskCommunication>	comTaskForMsgType;
	
	/** La instancia <i>singleton</i> de esta clase. */
	private static final TaskCommFactory			INSTANCE	= new TaskCommFactory();
	
	/**
	 * Constructor por defecto. Privado, por tratarse de un <i>singleton</i>.
	 */
	private TaskCommFactory() {
		comTaskForMsgType = new Hashtable<String, TaskCommunication>();
		
		try {
			
			IMessage enterWorld = MessageFactory.getInstance().createMessage(
					MsgTypes.MSG_ENTER_WORLD_TYPE);
			TEnterWorld tEnterWorld = new TEnterWorld(enterWorld);
			addComTask(tEnterWorld);
			
			IMessage getDynamicEntity = MessageFactory.getInstance()
					.createMessage(MsgTypes.MSG_GET_DYNAMIC_ENTITY_TYPE);
			TGetDynamicEntity tGetDynamicEntity = new TGetDynamicEntity(
					getDynamicEntity);
			addComTask(tGetDynamicEntity);
			
			IMessage move = MessageFactory.getInstance().createMessage(
					MsgTypes.MSG_MOVE_SEND_TYPE);
			TMove tMove = new TMove(move);
			addComTask(tMove);
			
			IMessage rotate = MessageFactory.getInstance().createMessage(
					MsgTypes.MSG_MOVE_SEND_TYPE);
			TRotate tRotate = new TRotate(rotate);
			addComTask(tRotate);
			
		} catch (UnsopportedMessageException e) {
			// No deberia suceder
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Retorna el hash de tipos de mensaje y tarea correspondiente. NOTA: El
	 * hash retornado es el mismo que el que soporta la estructura interna, por
	 * lo que cambios al mismoas, afectaran el comporamiento de la instancia.
	 * 
	 * @return el hash
	 */
	public Hashtable<String, TaskCommunication> getComTaskForType() {
		return comTaskForMsgType;
	}
	
	/**
	 * Especifica directamente el hash completo que mapea tipos de mensaje y
	 * tareas encargadas de tratarlos. Se perderá cualquier otra asociación
	 * previa.
	 * 
	 * @param comTaskForMsgTypeHash el hash a establecer
	 */
	public void setComTaskForType(
			final Hashtable<String, TaskCommunication> comTaskForMsgTypeHash) {
		
		this.comTaskForMsgType = comTaskForMsgTypeHash;
	}
	
	/**
	 * Devuelve la única instancia de esta clase.
	 * 
	 * @return La instancia.
	 */
	public static TaskCommFactory getInstance() {
		return INSTANCE;
	}
	
	/**
	 * Método para fabricar una instancia de la clase de tarea apropiada según
	 * el tipo del mensaje pasado como parámetro. La tarea de comunicación en
	 * cuestión estará inicializada con el mensaje dado.
	 * 
	 * @param msg el mensaje a usar
	 * @return la nueva tarea
	 */
	public final TaskCommunication createComTask(final IMessage msg) {
		return this.comTaskForMsgType.get(msg.getType()).factoryMethod(msg);
	}
	
	/**
	 * Añade una asociación, mapeando el tipo de mensaje de la tarea {@code
	 * comTask} con ella misma.<BR/>
	 * NOTA: si comTask no tiene seteado un tipo, se arrojara
	 * {@link NullPointerException}.<BR/>
	 * Si internamente ya existe una tarea asociada al tipo de mensaje que se
	 * agregara, se "pisara la referencia".
	 * 
	 * @param comTask tarea de comunicación correspondiente asociado a su tipo
	 *        de mensaje interno.
	 */
	public final void addComTask(final TaskCommunication comTask) {
		this.comTaskForMsgType.put(comTask.getMsgType(), comTask);
	}
	
	/**
	 * Elimina una asociación del hash para un tipo de mensaje dado. Si ese tipo
	 * no se encuentra como clave del hash, no se hace nada.
	 * 
	 * @param msgType tipo de mensaje al que se quitará su asociación
	 * 
	 * @return la tarea correspondiente o <code>null</code> si no había alguna
	 *         mapeada para el tipo de mensaje dado
	 */
	public final TaskCommunication removeComTask(final String msgType) {
		return this.comTaskForMsgType.remove(msgType);
	}
	
	/**
	 * Verifica si para el tipo de mensaje especificado se tiene una tarea
	 * asociada.
	 * 
	 * @param msgType tipo de mensaje
	 * 
	 * @return <code>true</code> si y solo si el tipo de mensaje está mapeado.
	 */
	public final boolean containsMsgType(final String msgType) {
		return this.comTaskForMsgType.containsKey(msgType);
	}
	
}
