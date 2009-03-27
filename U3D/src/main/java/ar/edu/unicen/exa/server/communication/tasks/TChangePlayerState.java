package ar.edu.unicen.exa.server.communication.tasks;

import ar.edu.unicen.exa.server.grid.Cell;
import ar.edu.unicen.exa.server.grid.IGridStructure;
import ar.edu.unicen.exa.server.player.Player;

import com.sun.sgs.app.ClientSession;
import common.messages.IMessage;
import common.messages.MsgTypes;
import common.messages.notify.MsgChangePlayerState;

/**
 * Tarea relacionada al mensaje de movimiento {@link MsgChangePlayerState}.<br/>
 * Deberá actualizar el estado de la entidad afectada, y reenviar el mensaje a
 * través las celdas ({@link Cell}) pertinentes.
 * 
 * @encoding UTF-8.
 */
public final class TChangePlayerState extends TaskCommunication {
	
	/**  Para cumplir con la version de la clase {@Serializable}. */
	private static final long serialVersionUID = -2534173752248112080L;

	/**
	 * Constructor que inicializa el estado interno de la tarea con el 
	 * parámetro.
	 * 
	 * @param msg El mensaje de la instancia.
	 */
	public TChangePlayerState(final IMessage msg) {
		super(msg);
	}
	
	/**
	 * Crear y devuelve una instancia de la clase.
	 * 
	 * @param msg El mensaje con el que trabajará la tarea internamente.
	 * @return Una instancia de esta clase.
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
		// Instancia del jugador.
		Player player = getPlayerAssociated();
		
		// Castear al mensage que corresponda.
		MsgChangePlayerState msg = (MsgChangePlayerState) getMessage();
		
		player.setState(msg.getNewState());
		
		
		// Recuperar la celda actual.
		Cell actualCell = getCellAssociated();

		// Crear el mensaje de notificación cambio de estado.
		msg.setType(MsgTypes.MSG_CHANGE_PLAYER_STATE_NOTIFY_TYPE);
		
		ClientSession session = player.getSession();
		// Notificar a la misma celda que el jugador se movió.
		actualCell.send(msg, session);
		
		// Obtener la estructura del mundo actual.
		IGridStructure structure = actualCell.getStructure();
		
		Cell[] adyacentes = structure.getAdjacents(
				actualCell, 
				player.getPosition()
			);
		
		if (adyacentes != null) {
			// Notificar a las celdas adyacentes.
			for (int i = 0; i < adyacentes.length; i++) {
				adyacentes[i].send(msg, null);
			}
		}
	}
	
}
