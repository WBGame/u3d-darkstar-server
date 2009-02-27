package ar.edu.unicen.exa.server.communication.tasks;

import com.jme.math.Vector3f;
import com.sun.sgs.app.ClientSession;

import ar.edu.unicen.exa.server.grid.Cell;
import ar.edu.unicen.exa.server.grid.GridManager;
import ar.edu.unicen.exa.server.grid.IGridStructure;
import ar.edu.unicen.exa.server.player.Player;
import common.exceptions.UnsopportedMessageException;
import common.messages.IMessage;
import common.messages.MessageFactory;
import common.messages.MsgPlainText;
import common.messages.MsgTypes;
import common.messages.notify.MsgChangeWorld;

/**
 * La tarea se ejecutará al recibir un mensaje por un canal desde un cliente,
 * el cual informa que cambiará al mundo indicado en el mensaje. Las acciones a
 * tomar son las siguientes:
 * <ol>
 * <li>Agregar el {@link Player} al mundo({@link IGridStructure}) deseado.</li>
 * <li>Suscribirlo a la celda({@link Cell}) por defecto del mundo y a las 
 * adyacentes según corresponda.</li>
 * <li>Quitarlo del mundo en el que estaba el {@link Player} previamente.</li>
 * <li>Desuscribirlo de las celdas en las que se encontraba en el mundo
 * anterior.</li>
 * <li>Enviar los mensajes correspondientes {@link MsgArrived} a las celdas del
 * mundo nuevo.</li>
 * <li>Enviar los mensajes correspondientes {@link MsgLeft} a las celdas del
 * mundo viejo.</li>
 * </ol>
 * 
 * @encoding UTF-8.
 * 
 */
public final class TEnterWorld extends TaskCommunication {

	/**  Para cumplir con la version de la clase {@Serializable}. */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor.
	 * 
	 * @param msg
	 *            El mensaje.
	 */
	public TEnterWorld(final IMessage msg) {
		super(msg);
	}

	/**
	 * Crea la tarea a travez del factory..
	 * 
	 * @param msg
	 *            El mensaje.
	 * 
	 * @return TEnterWorld.
	 */
	@Override
	public TaskCommunication factoryMethod(final IMessage msg) {
		return new TEnterWorld(msg);
	}

	/**
	 * Este metodo cambia de mundo({@link IGridStructure}) al {@link Player}. 
	 * Para ello, se obtiene el mundo actual y la celda({@link Cell}) donde se 
	 * encuentra. Luego se crea el mensaje {@link MsgLeft} para notificar al 
	 * los {@link Player}s que estan en la misma celda y en las visibles por
	 * otros, que el {@link Player} no se encuntra en la celda y finalmente se
	 * desuscribe del canal asociado a la celda. Para ingresar al nuevo mundo,
	 * se obtiene el id contenido en el mensaje y se asigna al {@link Player}
	 * la posicion y celda inicial suscribiendolo al canal de dicha celda. 
	 * Finalmente se crea el mensaje {@link MsgArrived} para 
	 * notificar a los demas {@link Player}s el ingreso de este {@link Player}.
	 * 
	 * @author Pablo Inchausti <inchausti.pablo at gmail dot com/>
	 * @encoding UTF-8.
	 * 
	 */
	public void run() {
		
		// Salir del mundo actual.
		
		// Instancia del jugador.
		Player player = getPlayerAssociated();
		// Recuperar la celda actual.
		Cell cell = getCellAssociated();
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
		// Obtener la sesion del jugador
		ClientSession session = player.getSession();
		// Notificar a la celda actual que la entidad dinamica ha salido
		cell.send(msgLeft, session);
		// Obtener la estructura del mundo actual.
		IGridStructure structure = cell.getStructure();
		// Obtener los adyacentes de la celda actual.
		Cell[] adyacentes = structure
				.getAdjacents(cell, player.getPosition());
		// Notificar a las celdas adyacentes que el jugador no se encuentra en
		// la celda.
		if (adyacentes != null) {
			for (int i = 0; i < adyacentes.length; i++) {
					adyacentes[i].send(msgLeft, null);
			}
		} 
		// Desuscribir al jugador de la celda del viejo mundo.
		cell.leaveFromChannel(session);

		// Entrada al nuevo mundo.
		
		// Casting al tipo de mensaje correspondiente.
		MsgChangeWorld msg = (MsgChangeWorld) getMessage();
		// Recuperar el id del nuevo mundo desde el mensaje recibido.
		String newWorldID = msg.getIdNewWorld();
		// Actualizar el jugador con el id del nuevo mundo.
		player.setActualWorld(newWorldID);
		// Definicion del angulo por defecto.
		player.setAngle(new Vector3f(1, 1, 1));
		// Obtener la estructura del nuevo mundo.
		structure = GridManager.getInstance()
				.getStructure(newWorldID);
		// Establecer la posicion inicial del jugador dentro del mundo.
		player.setPosition(msg.getSpownPosition());
		// Obtener la celda por defecto a partir del nuevo mundo.
		cell = structure.getCell(player.getPosition());

		// Suscribir al jugador a la nueva celda.
		cell.joinToChannel(session);
		
		// Crear el mensaje de ingreso al mundo por defecto.
		IMessage msgArrived = null;
		try {
			msgArrived = MessageFactory.getInstance()
				.createMessage(MsgTypes.MSG_ARRIVED_TYPE);
			// Seteo el mensaje con el id del jugador.
			((MsgPlainText) msgArrived).setMsg(player.getIdEntity());			
		} catch (UnsopportedMessageException e) {
			e.printStackTrace();
		}
		// Notificar a la celda por defecto que ingresó el jugador.
		cell.send(msgArrived, session);
		// Obtener los adyacentes de la nueva celda.
		adyacentes = structure
			.getAdjacents(cell, player.getPosition());
		// Notificar a las celdas adyacentes que ingresó el jugador.
		if (adyacentes != null) {
			for (int i = 0; i < adyacentes.length; i++) {
				adyacentes[i].send(msgArrived, null);
			}
		}
	}
}
