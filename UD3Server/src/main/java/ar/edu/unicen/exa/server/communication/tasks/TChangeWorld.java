package ar.edu.unicen.exa.server.communication.tasks;

import ar.edu.unicen.exa.server.communication.processors.ServerMsgProcessor;
import ar.edu.unicen.exa.server.communication
		 .processors.ServerMsgProssesorFactory;
import ar.edu.unicen.exa.server.grid.Cell;
import ar.edu.unicen.exa.server.grid.GridManager;
import ar.edu.unicen.exa.server.grid.IGridStructure;
import ar.edu.unicen.exa.server.player.Player;

import com.sun.sgs.app.ClientSession;
import common.exceptions.UnsopportedMessageException;
import common.messages.IMessage;
import common.messages.MessageFactory;
import common.messages.MsgPlainText;
import common.messages.MsgTypes;

/**
 * La tarea se ejecutará al recibir un mensaje por un canal desde un cliente, el
 * cual informa que cambiará al mundo indicado en el mensaje. Las acciones a
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
public final class TChangeWorld extends TaskCommunication {

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
	public TChangeWorld(final IMessage msg) {
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
		return new TChangeWorld(msg);
	}

	/**
	 * Este metodo cambia de mundo al jugador. Para ello, se obtiene el mundo
	 * actual y la celda donde se encuentra. Luego se crea el mensaje
	 * {@link MsgLeft} para notificar al los jugadores que estan en la misma
	 * celda y en las visivebles por otros, que el jugador no se encuntra en la
	 * celda y finalmente se desuscribe del canal asociado a la celda. Para
	 * ingresar al nuevo mundo, hacemos uso del procesador correspondiente al
	 * mensaje {@link MsgEnterWorld}, que contiene la funcionalidad para
	 * actualizar el nuevo mundo, suscribir al nuevo canal, notificar la llegada
	 * del jugador a las correspondientes celdas.
	 * 
	 * @author Pablo Inchausti <inchausti.pablo at gmail dot com/>
	 * 
	 */
	public void run() {
		// instancia del jugador
		Player player = getPlayerAssociated();
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

		// recuperar la celda actual
		Cell cell = getCellAssociated();
		// obtener la sesion del jugador
		ClientSession session = player.getSession();
		// notificar a la celda actual que la entidad dinamica ha salido
		cell.send(msgLeft, session);
		// obtener la estructura del mundo actual		
		IGridStructure structure = cell.getStructure();
		// obtener los adyacentes de la celda actual
		/*
		 * Cell[] adyacentes = structure .getAdjacents(cell,
		 * player.getPosition()); //notificar a las celdas adyacentes que el
		 * jugador no se encuntra en //la celda if (adyacentes != null) { for
		 * (int i = 0; i < adyacentes.length; i++) adyacentes[i].send(msgLeft,
		 * session); }
		 */
		// desuscribir al jugador de la celda del viejo mundo
		cell.leaveFromChannel(session);

		// entrar al nuevo mundo

		IMessage iMsg = getMessage();
		iMsg.setType(MsgTypes.MSG_ENTER_WORLD_TYPE);

		ServerMsgProcessor processor = 
				(ServerMsgProcessor) ServerMsgProssesorFactory
				.getInstance().createProcessor(MsgTypes.MSG_ENTER_WORLD_TYPE);

		processor.setPlayerAssociated(player);

		processor.process(iMsg);

	}
}
