package ar.edu.unicen.exa.server.communication.tasks;

import java.util.logging.Logger;

import ar.edu.unicen.exa.server.grid.Cell;
import ar.edu.unicen.exa.server.grid.GridManager;
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
 * @author Sebastián Perruolo &lt;sebastianperruolo at gmail dot com &gt;
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
		// Castear al mensage que corresponda.
		MsgMove msg = (MsgMove) getMessage();
		// Obtener la posicion destino
		Vector3f posDestino = msg.getPosDestino();
		Vector3f posOrigen = msg.getPosOrigen();
		if (playerOutOfBounds(player, posDestino)) {
			return;
		}

		
		// Recuperar la celda actual.
		Cell actualCell = getCellAssociated();
		Cell cellOrigen = actualCell;
		LOGGER.info("Movimiento: '" + player.getIdEntity() + "' (" 
				+ player.getPosition().getX() + ","
				+ player.getPosition().getY() + ","
				+ player.getPosition().getZ() + ") -> ("
				+ posDestino.getX() + ","
				+ posDestino.getY() + ","
				+ posDestino.getZ() + ")"
			);
		
		// Actualizamos la posicion del player
		player.setPosition(posDestino);

		// Obtener la estructura del mundo actual.
		IGridStructure structure = actualCell.getStructure();

		// Session del player.
		ClientSession session = player.getSession();
		// Si el jugador cambio de celda.
		if (!actualCell.isInside(posDestino)) {
			
			// Crear el mensaje de partida del jugador.
			IMessage msgLeft = createMsgLeft(player.getIdEntity());
			
			// lo envio por la celda que ya no ocupo
			actualCell.send(msgLeft, player.getSession());
			
			Cell[] adyacentes = structure.getAdjacents(
					actualCell, player.getPosition()
			);

			if (adyacentes != null) {
				// Notificar a las celdas visibles que el jugador se retiró
				for (int i = 0; i < adyacentes.length; i++) {
					adyacentes[i].send(msgLeft, null);
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

		Cell[] adyacentesAnteriores = structure.getAdjacents(cellOrigen, 
				posOrigen);
		
		Cell[] adyacentesNuevas = structure.getAdjacents(actualCell,
				player.getPosition());

		if (adyacentesNuevas != null) {
			// Notificar a las celdas visibles que el jugador se movió
			for (int i = 0; i < adyacentesNuevas.length; i++) {
				LOGGER.info("Reenviando mensaje a la celda adyacente: '" 
						+ adyacentesNuevas[i].getId() + "' del movimiento de '" 
						+ player.getIdEntity() + "' en la celda '"
						+ actualCell.getId() + "'");
				adyacentesNuevas[i].send(msg, null);
			}
		}
		// Crear el mensaje de partida del jugador.
		IMessage msgLeft = createMsgLeft(player.getIdEntity());
		if (adyacentesAnteriores != null) {
			//a las celdas que antes eran adyacentes y ahora no lo son,
			//se les avisa que el player se fue de su visión
			for (int i = 0; i < adyacentesAnteriores.length; i++) {
				boolean hadToSend = true;
				if (adyacentesNuevas != null) {
					for (int j = 0; j < adyacentesNuevas.length; j++) {
						if (adyacentesAnteriores[i].equals(
								adyacentesNuevas[j])) {
							hadToSend = false;
						}
					}
				}
				if (hadToSend) {
					adyacentesAnteriores[i].send(msgLeft, null);
					LOGGER.info(player.getIdEntity() + " -> Se fue de " 
							+ adyacentesAnteriores[i].getId());
				}
			}
		}
	}

	/**
	 * Evalua si la posición de destino del player se encuentra fuera de la 
	 * estructura.
	 * 
	 * @param player Player que se movió.
	 * @param posDestino Posición de destino del movimiento.
	 * @return true si el Player se movió hacia afuera de la estructura.
	 */
	private boolean playerOutOfBounds(
			final Player player, 
			final Vector3f posDestino) {
		IGridStructure s = GridManager.getInstance().getStructure(
				player.getActualWorld()
			);
		if (s.getCell(posDestino) == null) {
			StringBuffer sb = new StringBuffer("Posición invalida: (");
			sb.append(posDestino.getX())
				.append(",")
				.append(posDestino.getY())
				.append(",")
				.append(posDestino.getZ())
				.append(")");
			LOGGER.severe(sb.toString());
			return true;
		}
		return false;
	}
	/**
	 * Crea el mensaje necesario.
	 * @param idEntity Id de la entidad que se está yendo.
	 * @return IMessage MSG_LEFT_TYPE
	 */
	private IMessage createMsgLeft(final String idEntity) {
		// Crear el mensaje de partida del jugador.
		IMessage msgLeft = null;
		try {
			msgLeft = MessageFactory.getInstance().createMessage(
					MsgTypes.MSG_LEFT_TYPE);
			// Seteo el id del jugador como mensaje del mismo
			((MsgPlainText) msgLeft).setMsg(idEntity);
		} catch (UnsopportedMessageException e1) {
			e1.printStackTrace();
		}
		return msgLeft;
	}
}
