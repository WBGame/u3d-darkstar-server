package ar.edu.unicen.exa.server.communication.tasks;

import com.sun.sgs.app.AppContext;
import com.sun.sgs.app.ClientSession;

import ar.edu.unicen.exa.server.grid.Cell;
import ar.edu.unicen.exa.server.grid.GridManager;
import ar.edu.unicen.exa.server.grid.IGridStructure;
import ar.edu.unicen.exa.server.player.Player;
import common.messages.IMessage;
import common.messages.MsgTypes;
import common.messages.MsgPlainText;

/**
 * La tarea se ejecutará al recibir un mensaje directo desde un cliente, el cual
 * informa que entrará al mundo indicado en el mensaje. Las acciones a tomar son
 * las siguientes:
 * <ol>
 * 	<li>Agregar el jugador al mundo deseado.</li>
 * 	<li>Suscribirlo a la celda por defecto del mundo y a las adyacentes según
 * corresponda.</li>
 * 	<li>Quitarlo del mundo en el que estaba el jugador previamente.</li>
 * 	<li>Desuscribirlo de las celdas en las que se encontraba en el mundo 
 * anterior.</li>
 * 	<li>Enviar los mensajes correspondientes {@link MsgArrived} a las celdas 
 * del mundo nuevo.</li>
 * 	<li>Enviar los mensajes correspondientes {@link MsgLeft} a las celdas del 
 * mundo viejo.</li>
 * </ol>
 * 
 */
public final class TEnterWorld extends TaskCommunication {

	/**
	 * class Constructor.
	 * 
	 * @param msg the msg
	 */
	public TEnterWorld(final IMessage msg) {
		super(msg);
	}

	/**
	 *Create the Task across the factory.
	 * 
	 * @param msg the msg.
	 * 
	 * @return TEnterWorld
	 */
	@Override
	public TaskCommunication factoryMethod(final IMessage msg) {
		return new TEnterWorld(msg);
	}

	/**
	 * TODO javadoc. 
	 */
	public void run() {
		/**
		 * FIXME acomodar este método basándose en TMove.
		 *  el método MsgPlainText.getMsg(); retorna el id
		 *  del nuevo mundo.
		 * 
		 */
		String strNewWorld;
		String msgReport;
		strNewWorld = new String();
		msgReport = new String();
		//FIXME handle exception and common errors
		if (!MsgTypes.MSG_ENTER_WORLD_TYPE.equals(getMsgType())) {
			//throw El mensaje no me sirve para esta tarea!
			msgReport = "Message Usseless for this taks!";
			System.err.println(msgReport);
		}

		//if is a MsgPlainText
		MsgPlainText msg = (MsgPlainText) getMessage();

		String userId = super.getPlayerAsociete().getIdEntity();

		Player player = null;
		try {
			player = (Player) AppContext.getDataManager().getBinding(userId);
		} catch (Exception e) {
			//TODO Create exception if player {@link userId} cannot be found
			msgReport = "User <" + userId + "> Cannot be found";
			System.err.println(msgReport);
		}
		//Obtain the IIGridStructure for the player
		IGridStructure structure = GridManager.getInstance()
		.getStructure(player.getActualWorld());

		//Obtain the actual player cell
		Cell current = structure.getCell(player.getPosition());
		if (current == null) {
			//TODO Create exception if detect player outside the board \
			msgReport = "Player outside the board";
			System.err.println(msgReport);
		}
		ClientSession session = player.getSession();
		current.send(msg, session);
		Cell[] adyacentes = structure
		.getAdjacents(current, player.getPosition());

		//verify the adjacent
		if (adyacentes == null) {
			return;
		}
		if (adyacentes.length == 0) {
			return;
		} 
		//unsubscribe client from the adjacent cells
		for (int i = 0; i < adyacentes.length; i++) {
			adyacentes[i].leaveFromChannel(session);
		}
		//unsubscribe client from the actual cell
		current.leaveFromChannel(session);

		//obtain from the message the new world id
		strNewWorld = msg.getMsg();
		// set the new world for the user
		player.setActualWorld(strNewWorld);
		// update the structure
		structure = GridManager.getInstance().getStructure(strNewWorld);
		// Set the current Cell as the Spawn Cell from the new world
		current = structure.getSpawnCell();
		//And join the channel
		current.joinToChannel(session);
		//Notify to the player near the current cell
		current.send(msg, session);
		//get the adjacent cells for the spawn cell
		adyacentes = structure.getAdjacents(current , player.getPosition());
		//and join his channels
		for (int i = 0; i < adyacentes.length; i++) {
			adyacentes[i].joinToChannel(session);
		}

	}


}
