

package ar.edu.unicen.exa.server.communication.tasks;

import com.sun.sgs.app.ClientSession;
import ar.edu.unicen.exa.server.grid.Cell;
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
 */
public final class TRotate extends TaskCommunication {

	/**  Para cumplir con la version de la clase Serializable. */
	
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
	 * el mensaje a la celda actual del jugador. No es necesario enviar el 
	 * mensaje a las celdas adyacentes ya que nunca cambia de celda cuando
	 * el jugador rota.
	 */
	
	public void run() {
		
		//instancia del jugador
		Player player = getPlayerAssociated();
		
		//recuperar la celda actual
		Cell cell = getCellAssociated();
  	    		
		//castear al mensage de rotacion
		MsgRotate msg = (MsgRotate) getMessage();

		//actualizar el angulo del player
		player.setAngle(msg.getAngle());

		ClientSession session = player.getSession();
		
		//crear el mensaje de notificacion
		msg.setType(MsgTypes.MSG_ROTATE_NOTIFY_TYPE);
		
		//reenviar el mendaje de rotacion a la celda actual del jugador 
		cell.send(msg, session);
	}		
}
