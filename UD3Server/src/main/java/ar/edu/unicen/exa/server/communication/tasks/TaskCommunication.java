/**
 * CommunicationTask.java
 * 
 * @author lito
 */
package ar.edu.unicen.exa.server.communication.tasks;

import java.io.Serializable;
import ar.edu.unicen.exa.server.entity.DynamicEntity;
import ar.edu.unicen.exa.server.grid.Cell;
import ar.edu.unicen.exa.server.player.Player;

import com.sun.sgs.app.AppContext;
import com.sun.sgs.app.ManagedReference;
import com.sun.sgs.app.Task;
import common.messages.IMessage;

/**
 * La clase representa la base de la jerarquia de tareas relacionadas a la
 * ejecucion de acciones correspondientes a comunicacion y mensajeria del
 * servidor.<BR/>
 * Abstrae el comportamiento de las tareas de comunicacion del servidor, al
 * definir una {@link ManagedReference} (a un {@link DynamicEntity}) y una
 * {@link ManagedReference} (a una {@link Cell}) que representan las entidades
 * relacionadas a la recepsion y/o envio de los mensajes resultado de la
 * ejecucion de las acciones correspondientes a las sublcalses de tareas de
 * comunicacion.
 * 
 * @author lito
 */
//XXX debe implementar serializable debido a que las tareas se serializan
//durante el scheduling de la misma.
public abstract class TaskCommunication implements Task, Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * El mensaje relacionado.<BR/>
	 * Se declara transient, para obligar a que las sublcalses, declaren campos
	 * internos relacionados con los datos del mensaje, los cuales deben
	 * setearse en el constructor (que se invoca con un mensaje), de esta
	 * manera, sera mas eficiente la ejecucion del metodo run, ya que no tendra
	 * que realizar indirecciones a travez del mensaje para obtener los datos
	 * que necesite ya que los mismos seran campos internos.
	 */
	//XXX desabilito la opcion transient debido a que los mensajes en en el 
	//metodo run() de las tareas llegan con null
	private /*transient*/ IMessage			message;
	
	/** Referencia al player relacionado al mensaje a procesar. */
	private ManagedReference<Player>	playerAssociated = null;
	
	/** Referencia a la celda relacionada al mensaje a procesar. */
	private ManagedReference<Cell>	cellAssociated = null;
	
	/** El tipo de mensaje, dado que el mensaje es transient. */
	private String	msgType;

	/**
	 * Constructor que inicializa el estado interno con el parámetro. Setea el
	 * mensaje interno con el parámetro y el tipo de mensaje de la tarea con el
	 * tipo del mensaje pasado.
	 * 
	 * @param msg El mensaje de la instancia
	 */
	public TaskCommunication(final IMessage msg) {
		this.message = msg;
		msgType = msg.getType();
	}
	
	/**
	 * Constructor por defecto, de usarse, luego se debe setear manualmente el
	 * mensaje.
	 */
	public TaskCommunication() { }
	
	/**
	 * Retorna el mensaje de esta tarea.
	 * 
	 * @return el mensaje.
	 */
	public final IMessage getMessage() {
		return message;
	}
	
	/**
	 * Setea el mensaje de esta tarea.
	 * 
	 * @param aMessage el mensaje a setear.
	 */
	public final void setMessage(final IMessage aMessage) {
		this.message = aMessage;
	}
	
	/**
	 * @return el tipo de mensaje de esta tarea.
	 */
	public final String getMsgType() {
		return this.msgType;
	}
	
	/**
	 * Sigue el patrón de FactoryMethod, su implementación 
	 * debe crear y devolver una instancia de la clase implementadora.
	 * 
	 * @param msg El mensaje con el que trabajará la tarea internamente.
	 * 
	 * @return Una instancia de la clase implementadora.
	 */
	public abstract TaskCommunication factoryMethod(IMessage msg);
	
	/**
	 * @return Referencia al player relacionado al mensaje a procesar.
	 */
	public final Player getPlayerAssociated() {
		return playerAssociated.get();
	}
	
	/**
	 * @param associated Referencia al player relacionado al mensaje a
	 *        procesar.
	 */
	public final void setPlayerAssociated(final Player associated) {
		this.playerAssociated = AppContext.getDataManager().createReference(
				associated
		);
	}
	
	/**
	 * @return Referencia a la celda relacionada al mensaje a procesar.
	 */
	public final Cell getCellAssociated() {
		return cellAssociated.get();
	}
	
	/**
	 * This method allow to set the asociate cell.
	 * @param associated Referencia a la celda relacionada al mensaje a
	 *        procesar.
	 */
	public final void setCellAssociated(final Cell associated) {
		this.cellAssociated = AppContext.getDataManager().createReference(
				associated
		);
	}
}
