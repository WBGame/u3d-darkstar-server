package ar.edu.unicen.exa.server.communication.tasks;

import com.sun.sgs.app.AppContext;
import com.sun.sgs.app.ClientSession;

import ar.edu.unicen.exa.server.grid.Cell;
import ar.edu.unicen.exa.server.grid.GridManager;
import ar.edu.unicen.exa.server.grid.IGridStructure;
import ar.edu.unicen.exa.server.player.Player;
import common.messages.IMessage;
import common.messages.MsgTypes;
import common.messages.notify.MsgRotate;

/**
 * Tarea relacionada al mensaje de movimiento {@link MsgRotate}.<BR/>
 * Deberá actualizar el ángulo de rotación de la entidad afectada, y reenviar
 * el mensaje a través de las celdas pertinentes.
 * 
 * @author lito
 */
public final class TRotate extends TaskCommunication {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
     * Class Constructor.
     * 
     * @param msg the msg
     */
	public TRotate(final IMessage msg) {
		super(msg);
	}

	/**
	 * This method allow to create a  new Task Communication.
	 * 
	 * @param msg the msg
	 * 
	 * @return  new Msg.
	 */
	@Override
	public TaskCommunication factoryMethod(final IMessage msg) {
		return new TRotate(msg);
	}
	/**
	 * 
	 */
	public void run() {
		String msgReport;
		msgReport = new String();
		//FIXME handle exception and common errors
		if (!MsgTypes.MSG_MOVE_SEND_TYPE.equals(getMsgType())) {
			//throw El mensaje no me sirve para esta tarea!
			msgReport = "Message Usseless for this taks!";
			System.err.println(msgReport);
		}

		//if is a MsgMove
		MsgRotate msg = (MsgRotate) getMessage();

		String userId = msg.getIdDynamicEntity();
		Player player = null;
		try {
			player = (Player) AppContext.getDataManager()
			.getBinding(userId);
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
		//Hold Client Session
		ClientSession session = player.getSession();		
		//Unsuscribe player adjacents cells
		Cell[] adyacentes = structure
		.getAdjacents(current, player.getPosition());
		for (int i = 0; i < adyacentes.length; i++) {
			adyacentes[i].leaveFromChannel(session);
		}
		//Hold the new adjacent after rotate
		current.send(msg, session);
		adyacentes = structure
		.getAdjacents(current, msg.getAngle());

		//verify the adjacent
		if (adyacentes == null) {
			return;
		}
		if (adyacentes.length == 0) {
			return;
		} 
		//Set Player new angle
		player.setAngle(msg.getAngle());
		//and join his channels
		for (int i = 0; i < adyacentes.length; i++) {
			adyacentes[i].joinToChannel(session);
		}
	}		
}
