package ar.edu.unicen.exa.server.communication.tasks;

import ar.edu.unicen.exa.server.grid.Cell;
import ar.edu.unicen.exa.server.grid.IGridStructure;
import ar.edu.unicen.exa.server.player.Player;
import com.sun.sgs.app.ClientSession;
import common.messages.IMessage;
import common.messages.MsgTypes;
import common.messages.notify.MsgMove;

/**
 * Tarea relacionada al mensaje de movimiento {@link MsgMove}.<BR/>
 * Deberá actualizar la posición de la entidad en movimiento, y realizar toda
 * la lógica asociada a la suscripción de celdas y el reenvio del mensaje a 
 * través de ellas.
 * 
 * @author lito
 */
public final class TMove extends TaskCommunication {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1351514003104851241L;

	/**
	 * Constructor que inicializa el estado interno de la tarea con el 
	 * parámetro.
	 * 
	 * @param msg El mensaje de la instancia
	 */
	public TMove(final IMessage msg) {
		super(msg);
	}
	
	/**
	 * Crear y devuelve una instancia de la clase.
	 * 
	 * @param msg El mensaje con el que trabajará la tarea internamente.
	 * @return Una instancia de esta clase
	 */
	@Override
	public TaskCommunication factoryMethod(final IMessage msg) {
		return new TMove(msg);
	}
	
	/**
	 * Este método es el encargado de actualizar la posición del jugador,
	 * como así también enviar a las demás celdas el mensaje de notificación
	 * correspondiente al movimiento del mismo. En caso de que el jugador
	 * se cambie de celda, se realiza la desuscripción de la celda actual
	 * y se suscribe a la nueva celda. 
	 * 
	 * @author Roberto Kopp <robertokopp at hotmail dot com/>
	 * @author Sebastian Perruolo <sebastianperruolo at gmail dot com/>
	 * @author Pablo Inchausti <inchausti.pablo at gmail dot com/>
	 * 
	 * @encoding UTF-8
	 */
	public void run() {
		//instancia del jugador
		Player player = getPlayerAssociated();
		
		//recuperar la celda actual
		Cell actualCell = getCellAssociated();
		
		//Castear al mensage que corresponda
		MsgMove msg = (MsgMove) getMessage();

		//Actualizamos la posicion del player
		player.setPosition(msg.getPosDestino());

		//obtener la estructura del mundo actual
		IGridStructure structure = actualCell.getStructure();

		//obtner la celda destino
		Cell destino = structure.getCell(msg.getPosDestino());
		
		ClientSession session = player.getSession();
		//si el jugador cambio de celda
		if (!actualCell.getId().equals(destino.getId())) {
			//no habria que avisarle por medio de un mensaje a los restantes 
			//jugadores que se encuentran en la misma celda que el jugador? 
			//ya que no va a estar mas porque se cambio de celda o el jugador
			//desaparece de la escena por medio del frustum?
			actualCell.leaveFromChannel(session);
			destino.joinToChannel(session);
		}
		
		//crear el mensaje de llegada del jugador al nuevo mundo
		msg.setType(MsgTypes.MSG_MOVE_NOTIFY_TYPE);
		
		//notificar a la misma celda que el jugador se movió
		destino.send(msg, session);
		
		/*Cell[] adyacentes = structure.getAdjacents(destino,
				player.getPosition());
		
		//las siguientes líneas podrían formar una tarea por sí solas
		if (adyacentes != null) {
			//notificar a las celdas visibles que el jugador se movió
			for (int i = 0; i < adyacentes.length; i++) {
				adyacentes[i].send(msg, session);
			}
		}*/
	}
}
