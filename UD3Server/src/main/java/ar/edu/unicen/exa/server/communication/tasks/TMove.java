package ar.edu.unicen.exa.server.communication.tasks;

import ar.edu.unicen.exa.server.grid.Cell;
import ar.edu.unicen.exa.server.grid.GridManager;
import ar.edu.unicen.exa.server.grid.IGridStructure;
import ar.edu.unicen.exa.server.player.Player;

import com.sun.sgs.app.ClientSession;
import common.messages.IMessage;
import common.messages.MsgTypes;
import common.messages.notify.MsgMove;

/**
 * Tarea relacionada al mensaje de movimiento {@link MsgMove}.<BR/>
 * Deberá actualizar la posición de la entidad en movimiento, y realizar toda la
 * lógica asociada a la suscripción de celdas y el reenvio del mensaje a través
 * de ellas.
 * 
 */
public final class TMove extends TaskCommunication {
	
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
	 * TODO javadoc.
	 */
	public void run() {

		//Castear al mensage que corresponda
		MsgMove msg = (MsgMove) getMessage();
		Player player = getPlayerAsociete();
		Cell cell = getCellAsociete();
		ClientSession session = player.getSession();
		
		//Actualizamos la posicion del player
		player.setPosition(msg.getPosDestino());
		
		//debo obtener la IGridStructure del jugador
		IGridStructure structure = GridManager.getInstance()
				.getStructure(player.getActualWorld());
		
		Cell destino = structure.getCell(msg.getPosDestino());
		
		if (!cell.equals(destino)) {
			cell.leaveFromChannel(session);
			destino.joinToChannel(session);
			cell = destino;
		}
		
		
		//le seteo el tipo para que el cliente lo reciba.
		msg.setType(MsgTypes.MSG_MOVE_NOTIFY_TYPE);
		
		cell.send(msg, session);

		/*
		 * TODO refactorizar las siguientes líneas y ponerlas en Cell.send
		 * (si vale la pena) 
		 */

		Cell[] adyacentes = structure.getAdjacents(
				cell, 
				player.getPosition()
			);
		
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
