/**
 * 
 */
package ar.edu.unicen.exa.server.communication.processors;

import com.sun.sgs.app.AppContext;

import ar.edu.unicen.exa.server.communication.tasks.TaskCommFactory;
import ar.edu.unicen.exa.server.communication.tasks.TaskCommunication;
import common.messages.IMessage;
import common.processors.IProcessor;

// TODO: Auto-generated Javadoc
/**
 * Este Porcesador de mensajes, delega las acciones a tomar a tareas de
 * comunicacion de darkstar ({@link TaskCommunication}).<BR/>
 * Internamente, al momento de procesar un mensaje, crea la tarea
 * correspondiente mediante la fabrica de tareas ({@link TaskCommFactory}),
 * luego le setea los campos necesarios, y la submitea al task manager para ser
 * ejecutada.
 * 
 * @author lito
 * @see #process(IMessage)
 */
public class PTaskDelegator extends ServerMsgProcessor {
	
	/**
	 * Crea una nueva instancia de la clase, setendo sus variables internas en
	 * {@code null}.
	 */
	public PTaskDelegator() {
		
	}
	
	/**
	 * Crea una nueva instancia de la clase, setendo sus variables internas en
	 * {@code null}.<BR/>
	 * Luego de invocar este metodo, el invocador debera setear los campos
	 * internos mediante los setters de la clase antes de invocar el metodo
	 * {@link PTaskDelegator#process(IMessage)}.
	 * 
	 * @return the i processor
	 * 
	 * @see common.processors.IProcessor#factoryMethod()
	 */
	@Override
	public final IProcessor factoryMethod() {
		return new PTaskDelegator();
	}
	
	/**
	 * Crea la {@link TaskCommunication} correspondiente al mensaje pasado como
	 * parametro a travez de la {@link TaskCommFactory}, y la "submitea" al
	 * {@link TaskManager} para ser ejecutada.
	 * 
	 * @param msg El mensaje que se procesara.
	 * 
	 * @see common.processors.IProcessor#process(common.messages.IMessage)
	 */
	@Override
	public final void process(final IMessage msg) {
		TaskCommunication comT = TaskCommFactory.getInstance().createComTask(
				msg);
		
		comT.setCellAsociete(this.getCellAsociete());
		comT.setPlayerAsociete(this.getPlayerAsociete());
		
		AppContext.getTaskManager().scheduleTask(comT);
	}
	
}
