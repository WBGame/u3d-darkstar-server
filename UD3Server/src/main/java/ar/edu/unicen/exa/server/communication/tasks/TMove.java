package ar.edu.unicen.exa.server.communication.tasks;

import ar.edu.unicen.exa.server.grid.Cell;
import ar.edu.unicen.exa.server.grid.GridManager;
import ar.edu.unicen.exa.server.grid.IGridStructure;
import ar.edu.unicen.exa.server.player.Player;

import com.sun.sgs.app.AppContext;
import com.sun.sgs.app.ClientSession;
import common.messages.IMessage;
import common.messages.MsgTypes;
import common.messages.notify.MsgMove;

/**
 * Tarea relacionada al mensaje de movimiento {@link MsgMove}.<BR/>
 * Devera actualizar la posicion de la entidad en movimiento, y realizar toda la
 * logica asociada a la subscripcion de celdas y el reenvio del mensaje a travez
 * de ellas.
 * 
 */
public class TMove extends TaskCommunication {
	
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
	public final TaskCommunication factoryMethod(final IMessage msg) {
		return new TMove(msg);
	}
	
	/**
	 * //TODO javadoc.
	 */
	public final void run() {
		//FIXME manejar excepciones y errores comunes.
		if (!MsgTypes.MSG_MOVE_SEND_TYPE.equals(getMsgType())) {
			//throw El mensaje no me sirve para esta tarea!
			System.err.println("El mensaje no me sirve para esta tarea!");
		}
		
		//El mensaje es de tipo MsgMove
		MsgMove msg = (MsgMove) getMessage();

		String userId = msg.getIdDynamicEntity();
		Player player = null;
		try {
			player = (Player) AppContext.getDataManager()
					.getBinding(userId);
		} catch (Exception e) {
			//throw no pude encontrar al usuario <userId>
			System.err.println("No pude encontrar al usuario <" + userId + ">");
		}
		//debo obtener la IGridStructure del jugador
		IGridStructure structure = GridManager.getInstance()
				.getStructure(player.getActualWorld());

		//Obtengo la celda donde está el jugador
		Cell current = structure.getCell(msg.getPosOrigen());
		if (current == null) {
			//throw el jugador está afuera del tablero!
			System.err.println("El jugador está afuera del tablero!");
		}
		ClientSession session = player.getSession().get();
		current.send(msg, session);
		Cell[] adyacentes = structure
				.getAdjacents(current, msg.getPosDestino());
		
		//las siguientes líneas podrían formar una tarea por sí solas
		if (adyacentes == null) {
			return;
		}
		if (adyacentes.length == 0) {
			return;
		}
		for (int i = 0; i < adyacentes.length; i++) {
			adyacentes[i].send(msg, session);
		}
	}
	
}
