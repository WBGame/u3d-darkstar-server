package ar.edu.unicen.exa.server.communication.tasks;

import ar.edu.unicen.exa.server.grid.Cell;
import ar.edu.unicen.exa.server.grid.GridManager;
import ar.edu.unicen.exa.server.grid.IGridStructure;
import ar.edu.unicen.exa.server.player.Player;

import com.sun.sgs.app.ClientSession;
import common.messages.IMessage;
import common.messages.MsgTypes;
import common.messages.notify.MsgChangePlayerState;

/**
 * Tarea relacionada al mensaje de movimiento {@link MsgChangePlayerState}.<br/>
 * Deberá actualizar el estado de la entidad afectada, y reenviar
 * el mensaje a través las celdas pertinentes.
 * 
 */
public final class TChangePlayerState extends TaskCommunication {
	
	/** The version of the serialized form of this class. */
	private static final long serialVersionUID = -2534173752248112080L;

	/**
	 * Constructor que inicializa el estado interno de la tarea con el 
	 * parámetro.
	 * 
	 * @param msg El mensaje de la instancia
	 */
	public TChangePlayerState(final IMessage msg) {
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
		return new TChangePlayerState(msg);
	}
	
	/**
	 * Este método es el encargado de actualizar el estado del jugador,
	 * como así también enviar a las demás celdas el mensaje de notificación
	 * correspondiente. 
	 * 
	 */
	public void run() {
		//instancia del jugador
		Player player = getPlayerAssociated();
		
		//Castear al mensage que corresponda
		MsgChangePlayerState msg = (MsgChangePlayerState) getMessage();
		
		player.setState(msg.getNewState());
		
		
		//recuperar la celda actual
		Cell actualCell = getCellAssociated();

		//crear el mensaje de notificación cambio de estado
		msg.setType(MsgTypes.MSG_CHANGE_PLAYER_STATE_NOTIFY_TYPE);
		
		ClientSession session = player.getSession();
		//notificar a la misma celda que el jugador se movió
		actualCell.send(msg, session);
		
		//obtener la estructura del mundo actual
		IGridStructure structure = actualCell.getStructure();
		
		Cell[] adyacentes = structure.getAdjacents(
				actualCell, 
				player.getPosition()
			);
		
		if (adyacentes != null) {
			//notificar a las celdas adyacentes
			for (int i = 0; i < adyacentes.length; i++) {
				adyacentes[i].send(msg, session);
			}
		}
	}
	
}
