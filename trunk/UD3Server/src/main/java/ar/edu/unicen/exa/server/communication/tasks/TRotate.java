/**
 * 
 */
package ar.edu.unicen.exa.server.communication.tasks;

import com.sun.sgs.app.AppContext;
import com.sun.sgs.app.ClientSession;

import ar.edu.unicen.exa.server.grid.Cell;
import ar.edu.unicen.exa.server.grid.GridManager;
import ar.edu.unicen.exa.server.grid.IGridStructure;
import ar.edu.unicen.exa.server.player.Player;
import common.messages.IMessage;
import common.messages.MsgTypes;
import common.messages.notify.MsgMove;
import common.messages.notify.MsgRotate;

/**
 * Tarea relacionada al mensaje de movimiento {@link MsgRotate}.<BR/>
 * Devera actualizar el angulo de rotacion de la entidad afectada, y reenviar
 * el mensaje a travez las celdas pertinentes.
 * 
 * @author lito
 */
public class TRotate extends TaskCommunication {

	/**
	 * @param msg
	 */
	public TRotate(IMessage msg) {
		super(msg);
	}

	/**
	 * TODO hacer javaDoc
	 * 
	 * @param msg
	 * @return
	 */
	@Override
	public TaskCommunication factoryMethod(IMessage msg) {
		return new TRotate(msg);
	}

	public void run() {
		String strNewWorld;
		String msgReport;
		strNewWorld = new String();
		msgReport = new String();
		//FIXME handle exception and common errors
		if (!MsgTypes.MSG_MOVE_SEND_TYPE.equals(getMsgType())) {
			//throw El mensaje no me sirve para esta tarea!
			msgReport ="Message Usseless for this taks!";
			System.err.println(msgReport);
		}

		//if is a MsgMove
		MsgMove msg = (MsgMove) getMessage();

		String userId = msg.getIdDynamicEntity();
		Player player = null;
		try {
			player = (Player) AppContext.getDataManager()
			.getBinding(userId);
		} catch (Exception e) {
			//TODO Create exception if player {@link userId} cannot be found
			msgReport ="User <" + userId + "> Cannot be found";
			System.err.println(msgReport);
		}
		//Obtain the IIGridStructure for the player
		IGridStructure structure = GridManager.getInstance()
		.getStructure(player.getActualWorld());

		//Obtain the actual player cell
		Cell current = structure.getCell(msg.getPosOrigen());
		if (current == null) {
			//TODO Create exception if detect player outside the board \
			msgReport ="Player outside the board";
			System.err.println(msgReport);
		}
		ClientSession session = player.getSession();
		current.send(msg, session);
		Cell[] adyacentes = structure
		.getAdjacents(current, msg.getPosDestino());

		//verify the adjacent
		if (adyacentes == null) {
			return;
		}
		if (adyacentes.length == 0) {
			return;
		} 
		//TODO calculate the rotation
	}		
}