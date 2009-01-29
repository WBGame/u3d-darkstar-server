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
		
		//ver si el mensaje recibido sea el correspondiente para esta tarea
		if (!MsgTypes.MSG_CHANGE_PLAYER_STATE_SEND_TYPE.equals(getMsgType())) {
			throw new Error("Tipo de mensaje no válido para esta tarea");
		}
		
		//instancia del jugador
		Player player = getPlayerAssociated();
		
		//Castear al mensage que corresponda
		MsgChangePlayerState msg = (MsgChangePlayerState) getMessage();
		
		player.setState(msg.getNewState());
		
		//obtener la estructura del mundo actual
		IGridStructure structure = GridManager.getInstance()
				.getStructure(player.getActualWorld());
		
		//recuperar la celda actual
		Cell actualCell = structure.getCell(player.getPosition());
		
		Cell[] adyacentes = structure.getAdjacents(
				actualCell, 
				player.getPosition()
			);
		
		//crear el mensaje de notificación cambio de estado
		msg.setType(MsgTypes.MSG_CHANGE_PLAYER_STATE_NOTIFY_TYPE);
		
		ClientSession session = player.getSession();
		//notificar a la misma celda que el jugador se movió
		actualCell.send(msg, session);
		
		//notificar a las celdas adyacentes
		for (int i = 0; i < adyacentes.length; i++) {
			adyacentes[i].send(msg, session);
		}
	}
	
}
