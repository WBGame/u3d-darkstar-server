package ar.edu.unicen.exa.server.communication.processors;

import com.jme.math.Vector3f;
import com.sun.sgs.app.ClientSession;
import ar.edu.unicen.exa.server.grid.Cell;
import ar.edu.unicen.exa.server.grid.GridManager;
import ar.edu.unicen.exa.server.grid.IGridStructure;
import ar.edu.unicen.exa.server.player.Player;
import common.exceptions.UnsopportedMessageException;
import common.messages.IMessage;
import common.messages.MessageFactory;
import common.messages.MsgTypes;
import common.messages.MsgPlainText;
import common.processors.IProcessor;

/**
 * Este procesador se ejecutará al recibir un mensaje directo desde un cliente,
 * el cual informa que entrará al mundo indicado en el mensaje. Las 
 * acciones a tomar son las siguientes:
 * <ol>
 * 	<li>Agregar el jugador al mundo deseado.</li>
 * 	<li>Suscribirlo a la celda por defecto del mundo y a las adyacentes según
 * corresponda.</li>
 * 	<li>Enviar los mensajes correspondientes {@link MsgArrived} a las celdas 
 * del mundo.</li>
 * </ol>
 * 
 */
public final class PEnterWorld extends ServerMsgProcessor {

	/**
	 * Constructor por defecto, inicializa las variables internas en {@code
	 * null}.
	 */
	public PEnterWorld() { }
	
	/**
	 * Retorna un instancia de la clase, con sus campos internos inicializados
	 * en {@code null}.
	 * 
	 * @return the i processor
	 * 
	 * @see common.processors.IProcessor#factoryMethod()
	 */
	public IProcessor factoryMethod() {
		return new PEnterWorld();
	}

	/**
	 * TODO javadoc. 
	 * 
	 * @param msg mensaje a procesar
	 */
	public void process(final IMessage msg) {

		//ver si el mensaje recibido sea el correspondiente para esta tarea
		if (!MsgTypes.MSG_ENTER_WORLD_TYPE.equals(getMsgType())) {
			throw new Error("Tipo de mensaje no válido para esta tarea");
		}
		//casting al tipo de mensaje correspondiente
		MsgPlainText imsg = (MsgPlainText) msg;
		//recuperar el id del nuevo mundo desde el mensaje recibido
		String newWorldID = imsg.getMsg();
		//instancia del jugador
		Player player = getPlayerAssociated();
		// actualizar el jugador con el id del nuevo mundo
		player.setActualWorld(newWorldID);
		//definicion del angulo por defecto
		player.setAngle(new Vector3f(1, 1, 1));
		// obtener la estructura del nuevo mundo
		IGridStructure structure = GridManager.getInstance()
				.getStructure(newWorldID);
		//establecer la posicion inicial del jugador dentro del mundo
		player.setPosition(structure.getSpawnPosition());
		// obtener la celda por defecto a partir del nuevo mundo
		Cell cell = structure.getSpawnCell();
		//inicializo la posible sesion
		ClientSession session = null;
		//obtener la sesion del jugador
		session = player.getSession();
		// suscribir al jugador a la nueva celda
		cell.joinToChannel(session);
		
		//crear el mensaje de llegada del jugador al nuevo mundo
		IMessage msgArrived = null;
		try {
			msgArrived = MessageFactory.getInstance()
				.createMessage(MsgTypes.MSG_ARRIVED_TYPE);
			//seteo el mensaje con el id del jugador
			((MsgPlainText) msgArrived).setMsg(player.getIdEntity());			
		} catch (UnsopportedMessageException e) {
			e.printStackTrace();
		}
		//notificar a la celda por defecto que ingresó el jugador
		cell.send(msgArrived, session);
		//obtener los adyacentes de la nueva celda
		/*Cell[] adyacentes = structure
			.getAdjacents(cell, player.getPosition());
		//notificar a las celdas adyacentes que ingresó el jugador
		if (adyacentes != null) {
			for (int i = 0; i < adyacentes.length; i++) {
				adyacentes[i].send(msgArrived, session);
			}
		}
		*/
	}
}
