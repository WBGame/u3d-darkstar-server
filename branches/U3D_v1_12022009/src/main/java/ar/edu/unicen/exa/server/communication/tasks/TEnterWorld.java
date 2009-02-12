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

/**
 * La tarea se ejecutará al recibir un mensaje por un canal desde un cliente,
 * el cual informa que cambiará al mundo indicado en el mensaje. Las acciones a
 * tomar son las siguientes:
 * <ol>
 * <li>Agregar el jugador al mundo deseado.</li>
 * <li>Suscribirlo a la celda por defecto del mundo y a las adyacentes según
 * corresponda.</li>
 * <li>Quitarlo del mundo en el que estaba el jugador previamente.</li>
 * <li>Desuscribirlo de las celdas en las que se encontraba en el mundo
 * anterior.</li>
 * <li>Enviar los mensajes correspondientes {@link MsgArrived} a las celdas del
 * mundo nuevo.</li>
 * <li>Enviar los mensajes correspondientes {@link MsgLeft} a las celdas del
 * mundo viejo.</li>
 * </ol>
 * 
 */
public final class TEnterWorld extends TaskCommunication {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * class Constructor.
	 * 
	 * @param msg
	 *            the msg
	 */
	public TEnterWorld(final IMessage msg) {
		super(msg);
	}

	/**
	 *Create the Task across the factory.
	 * 
	 * @param msg
	 *            the msg.
	 * 
	 * @return TEnterWorld
	 */
	@Override
	public TaskCommunication factoryMethod(final IMessage msg) {
		return new TEnterWorld(msg);
	}

	/**
	 * Este metodo cambia de mundo al jugador. Para ello, se obtiene el mundo
	 * actual y la celda donde se encuentra. Luego se crea el mensaje
	 * {@link MsgLeft} para notificar al los jugadores que estan en la misma
	 * celda y en las visivebles por otros, que el jugador no se encuntra en la
	 * celda y finalmente se desuscribe del canal asociado a la celda. Para
	 * ingresar al nuevo mundo, se obtiene el id contenido en el mensaje y se
	 * asigna al jugador la posicion y celda inicial suscribiendolo al canal
	 * de dicha celda. Finalmente se crea el mensaje {@link MsgArrived} para 
	 * notificar a los demas jugadores el ingreso de este jugador.
	 * 
	 * @author Pablo Inchausti <inchausti.pablo at gmail dot com/>
	 * 
	 */
	public void run() {
		
		// salir del mundo actual
		
		// instancia del jugador
		Player player = getPlayerAssociated();
		// recuperar la celda actual
		Cell cell = getCellAssociated();
		// crear el mensaje de partida del jugador
		IMessage msgLeft = null;
		try {
			msgLeft = MessageFactory.getInstance().createMessage(
					MsgTypes.MSG_LEFT_TYPE);
			// seteo el id del jugador como mensaje del mismo
			((MsgPlainText) msgLeft).setMsg(player.getIdEntity());
		} catch (UnsopportedMessageException e1) {
			e1.printStackTrace();
		}

		// obtener la sesion del jugador
		ClientSession session = player.getSession();
			// notificar a la celda actual que la entidad dinamica ha salido
		cell.send(msgLeft, session);
		//obtener la estructura del mundo actual
		IGridStructure structure = cell.getStructure();
		//obtener los adyacentes de la celda actual
		Cell[] adyacentes = structure
				.getAdjacents(cell, player.getPosition());
		//notificar a las celdas adyacentes que el jugador no se encuntra en
		//la celda
		if (adyacentes != null) {
			for (int i = 0; i < adyacentes.length; i++) {
					adyacentes[i].send(msgLeft, session);
			}
		} 
		// desuscribir al jugador de la celda del viejo mundo
		cell.leaveFromChannel(session);

		// entrada al nuevo mundo
		
		//casting al tipo de mensaje correspondiente
		MsgPlainText msg = (MsgPlainText) getMessage();
		//recuperar el id del nuevo mundo desde el mensaje recibido
		String newWorldID = msg.getMsg();
		// actualizar el jugador con el id del nuevo mundo
		player.setActualWorld(newWorldID);
		//definicion del angulo por defecto
		player.setAngle(new Vector3f(1, 1, 1));
		// obtener la estructura del nuevo mundo
		structure = GridManager.getInstance()
				.getStructure(newWorldID);
		//establecer la posicion inicial del jugador dentro del mundo
		player.setPosition(structure.getSpawnPosition());
		// obtener la celda por defecto a partir del nuevo mundo
		cell = structure.getSpawnCell();

		// suscribir al jugador a la nueva celda
		cell.joinToChannel(session);
		
		//crear el mensaje de ingreso al mundo por defecto
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
		adyacentes = structure
			.getAdjacents(cell, player.getPosition());
		//notificar a las celdas adyacentes que ingresó el jugador
		if (adyacentes != null) {
			for (int i = 0; i < adyacentes.length; i++) {
				adyacentes[i].send(msgArrived, session);
			}
		}
	}
}
