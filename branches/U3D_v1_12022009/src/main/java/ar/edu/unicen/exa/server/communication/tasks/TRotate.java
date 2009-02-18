
package ar.edu.unicen.exa.server.communication.tasks;

import com.sun.sgs.app.ClientSession;
import ar.edu.unicen.exa.server.grid.Cell;
import ar.edu.unicen.exa.server.grid.IGridStructure;
import ar.edu.unicen.exa.server.player.Player;
import common.messages.IMessage;
import common.messages.MsgTypes;
import common.messages.notify.MsgRotate;

/**
 * Tarea relacionada al mensaje de movimiento {@link MsgRotate}.<BR/>
 * Debera actualizar el angulo de rotacion de la entidad afectada, y reenviar
 * el mensaje a travez las celdas pertinentes.
 * 
 * @author lito
 * @encoding UTF-8.
 */
public final class TRotate extends TaskCommunication {

	/**  Para cumplir con la version de la clase {@link Serializable}. */
	
	private static final long serialVersionUID = 1L;
	
	/**
     * Constructor.
     * 
     * @param msg El mensaje de la instancia.
     */
	public TRotate(final IMessage msg) {
		super(msg);
	}


	/**
	 * Este método permite crear un nuevo Grupo de Comunicación.
	 * 
	 * @param msg El mensaje con el que trabaja la tarea.
	 * 
	 * @return Retorna el mensaje.
	 */
	
	public TaskCommunication factoryMethod(final IMessage msg) {
		return new TRotate(msg);
	}
	
	/**
	 * Actualizar el angulo de rotacion de la entidad afectada, y reenviar
	 * el mensaje a travez las celdas pertinentes.
	 */
	
	public void run() {
		
		// Instancia del jugador.
		Player player = getPlayerAssociated();
		
		// Recuperar la celda actual.
		Cell cell = getCellAssociated();
  	    		
		// Castear al mensage de rotacion.
		MsgRotate msg = (MsgRotate) getMessage();

		// Actualizar el angulo del jugador.
		player.setAngle(msg.getAngle());

		ClientSession session = player.getSession();
		
		// Crear el mensaje de notificacion.
		msg.setType(MsgTypes.MSG_ROTATE_NOTIFY_TYPE);
		
		// Reenviar el mensaje de rotacion a la celda actual del jugador. 
		cell.send(msg, session);
		
		// Obtener la estructura del mundo actual.
		IGridStructure structure = cell.getStructure();
		
		Cell[] adyacentes = structure.getAdjacents(
				cell, 
				player.getPosition()
			);
		
		if (adyacentes != null) {
			// Notificar a las celdas adyacentes.
			for (int i = 0; i < adyacentes.length; i++) {
				adyacentes[i].send(msg, session);
			}
		}		
	}
}
