

package ar.edu.unicen.exa.server.communication.tasks;

import com.sun.sgs.app.AppContext;
import com.sun.sgs.app.ClientSession;

import ar.edu.unicen.exa.server.grid.Cell;
import ar.edu.unicen.exa.server.grid.GridManager;
import ar.edu.unicen.exa.server.grid.IGridStructure;
import ar.edu.unicen.exa.server.player.Player;
import common.messages.IMessage;
import common.messages.MsgTypes;
//import common.messages.notify.MsgMove;
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
	 * el mensaje a travez las celdas pertinentes.
	 */
	
	public void run() {
		String msgReport;
		msgReport = new String();

		//Ver si el mensaje recibido sea el correspondiente para esta tarea.
		if (!MsgTypes.MSG_MOVE_SEND_TYPE.equals(getMsgType())) {

			throw new Error("Mensaje no valido para esta tarea");
		}
        
		
		//Castear al mensage que corresponda.
		//En este caso casteamos a MsgRotate.
		MsgRotate msg = (MsgRotate) getMessage();
		
		//El identificador de la entidad que realiza la rotacion.
		String userId = msg.getIdDynamicEntity();
		
		
		Player player = null;
		
		try {
			player = (Player) AppContext.getDataManager()
			.getBinding(userId);
		} catch (Exception e) {
			//Crea excepcion si el jugador {@link userId} no se puede encontrar.
			msgReport = "Usuario <" + userId + "> no se puede encontar";
			System.err.println(msgReport);
		}
		
		//Obtener el IGridStructure para el jugador.
		IGridStructure structure = GridManager.getInstance()
		.getStructure(player.getActualWorld());

		//Obtener la celda del mundo actual.
		Cell current = structure.getCell(player.getPosition());
		if (current == null) {
			//TODO Create exception if detect player outside the board.
			msgReport = "Player outside the board";
			System.err.println(msgReport);
		}
		//Mantener la sesion del cliente.
		ClientSession session = player.getSession();		

		//Desinscribir al jugador de las celdas adyacentes.
		Cell[] adyacentes = structure
		.getAdjacents(current, player.getPosition());
		for (int i = 0; i < adyacentes.length; i++) {
			adyacentes[i].leaveFromChannel(session);
		}

		//Mantiene los nuevos adyacentes despues de la rotacion.
		current.send(msg, session);
		adyacentes = structure
		.getAdjacents(current, msg.getAngle());

		//Verificar los adyacentes
		if (adyacentes == null) {
			return;
		}
		if (adyacentes.length == 0) {
			return;
		} 

	    //Establece el angulo del jugador.
		player.setAngle(msg.getAngle());

		//Y se une a sus canales.
		for (int i = 0; i < adyacentes.length; i++) {
			adyacentes[i].joinToChannel(session);
		}
		
	}		
}
