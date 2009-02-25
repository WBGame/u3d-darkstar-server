package ar.edu.unicen.exa.server.communication.tasks;

import java.util.logging.Logger;

import ar.edu.unicen.exa.server.grid.Cell;
import ar.edu.unicen.exa.server.grid.IGridStructure;
import ar.edu.unicen.exa.server.player.Player;

import com.jme.math.Vector3f;
import com.sun.sgs.app.ClientSession;
import common.exceptions.UnsopportedMessageException;
import common.messages.IMessage;
import common.messages.MessageFactory;
import common.messages.MsgPlainText;
import common.messages.MsgTypes;
import common.messages.notify.MsgMove;

/**
 * Tarea relacionada al mensaje de movimiento {@link MsgMove}.<BR/>
 * Deberá actualizar la posición de la entidad en movimiento, y realizar toda
 * la lógica asociada a la suscripción de celdas y el reenvio del mensaje a 
 * través de ellas.
 * 
 * @author lito
 * @encoding UTF-8.
 */
public final class TMove extends TaskCommunication {

	/**  Para cumplir con la version de la clase {@Serializable}. */
	private static final long serialVersionUID = 1351514003104851241L;

	/** Logger. */
	private static final Logger LOGGER = Logger.getLogger(
			TMove.class.getName());
	
	/**
	 * Constructor que inicializa el estado interno de la tarea con el 
	 * parámetro.
	 * 
	 * @param msg El mensaje de la instancia.
	 */
	public TMove(final IMessage msg) {
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
		return new TMove(msg);
	}

	/**
	 * Este método es el encargado de actualizar la posición del {@link 
	 * player}, como así también enviar a las demás celdas el mensaje de
	 * notificación correspondiente al movimiento del mismo. En caso de que el
	 * {@link player} se cambie de celda({@link Cell}), se realiza la 
	 * desuscripción de la celda actual y se suscribe a la nueva celda. 
	 * 
	 * @author Roberto Kopp <robertokopp at hotmail dot com/>
	 * @author Sebastian Perruolo <sebastianperruolo at gmail dot com/>
	 * @author Pablo Inchausti <inchausti.pablo at gmail dot com/>
	 * 
	 * TODO review implementation.
	 * 
	 * @encoding UTF-8
	 */
	public void run() {
		// Instancia del jugador.
		Player player = getPlayerAssociated();

		// Recuperar la celda actual.
		Cell actualCell = getCellAssociated();

		// Castear al mensage que corresponda.
		MsgMove msg = (MsgMove) getMessage();

		// Obtener la posicion destino
		Vector3f posDestino = msg.getPosDestino();

		LOGGER.info("Posición anterior: (" 
				+ player.getPosition().getX() + ","
				+ player.getPosition().getY() + ","
				+ player.getPosition().getZ() + ")");		
		
		LOGGER.info("Posición recibida: (" 
				+ posDestino.getX() + ","
				+ posDestino.getY() + ","
				+ posDestino.getZ() + ")");
		
		// Actualizamos la posicion del player
		player.setPosition(posDestino);

		// Obtener la estructura del mundo actual.
		IGridStructure structure = actualCell.getStructure();

		// Session del player.
		ClientSession session = player.getSession();

		// Si el jugador cambio de celda.
		if (!actualCell.isInside(posDestino)) {
			
			// Crear el mensaje de partida del jugador.
			IMessage msgLeft = null;
			try {
				msgLeft = MessageFactory.getInstance().createMessage(
						MsgTypes.MSG_LEFT_TYPE);
				// Seteo el id del jugador como mensaje del mismo
				((MsgPlainText) msgLeft).setMsg(player.getIdEntity());
			} catch (UnsopportedMessageException e1) {
				e1.printStackTrace();
			}			
			
			// lo envio por la celda que ya no ocupo
			actualCell.send(msgLeft, player.getSession());
			
			Cell[] adyacentes = structure.getAdjacents(
					actualCell, player.getPosition()
			);

			if (adyacentes != null) {
				// Notificar a las celdas visibles que el jugador se retiró
				for (int i = 0; i < adyacentes.length; i++) {
					adyacentes[i].send(msg, session);
				}
			}			
			
			// salgo de la celda actual
			actualCell.leaveFromChannel(session);

			//obtener la celda destino
			actualCell = structure.getCell(posDestino);

			if (actualCell == null) {
				LOGGER.severe("Posición invalida: (" 
						+ posDestino.getX() + ","
						+ posDestino.getY() + ","
						+ posDestino.getZ() + ")");
				return;
			} 
			
			// cambio de canal al nuevo.
			actualCell.joinToChannel(session);
		}

		// Crear el mensaje de llegada del jugador al nuevo mundo.
		msg.setType(MsgTypes.MSG_MOVE_NOTIFY_TYPE);

		// Notificar a la misma celda que el jugador se movió.
		actualCell.send(msg, session);

		Cell[] adyacentes = structure.getAdjacents(actualCell,
				player.getPosition());

		if (adyacentes != null) {
			// Notificar a las celdas visibles que el jugador se movió
			for (int i = 0; i < adyacentes.length; i++) {
				adyacentes[i].send(msg, session);
			}
		}
	}
}
