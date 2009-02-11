package ar.edu.unicen.exa.server.player;

import java.io.Serializable;
import java.nio.ByteBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;

import ar.edu.unicen.exa.server.communication.processors.ServerMsgProcessor;
import ar.edu.unicen.exa.server.communication.processors
.ServerMsgProssesorFactory;
import ar.edu.unicen.exa.server.grid.Cell;
import ar.edu.unicen.exa.server.grid.GridManager;
import ar.edu.unicen.exa.server.grid.IGridStructure;

import com.sun.sgs.app.AppContext;
import com.sun.sgs.app.ClientSession;
import com.sun.sgs.app.ClientSessionListener;
import com.sun.sgs.app.DataManager;
import com.sun.sgs.app.ManagedReference;
import common.exceptions.MalformedMessageException;
import common.exceptions.UnsopportedMessageException;
import common.messages.IMessage;
import common.messages.MessageFactory;
import common.messages.MsgPlainText;
import common.messages.MsgTypes;



/** 
 * Implementación de {@code ClientSessionListener}. 
 * Está asociado uno a uno con cada jugador ( {@link Player} ) del sistema. 
 * Debe atender peticiones de desconexión y mensajes entrantes.
 * 
 * @author Pablo Inchausti <inchausti.pablo at gmail dot com/> 
 * @encoding UTF-8 
 */
public final class UserSessionListener 
implements ClientSessionListener, Serializable {

	/** The version of the serialized form of this class. */
	private static final long serialVersionUID = 4348700548351927735L;

	/** Logger para esta clase. */
	private static final Logger LOGGER = 
		Logger.getLogger(UserSessionListener.class.getName());

	/**
	 * Es una referencia {@code ManagedReference} al {@link Player}.
	 */
	private ManagedReference<Player> playerRef;

	/**
	 * Retorna un ( {@link Player} ) a partir de la referencia 
	 * {@code ManagedReference} al jugador asociado.
	 * 
	 * @return Player jugador que hace referencia el {@code ManagedReference}.
	 */
	public Player getPlayer() {
		return playerRef.get();
	}

	/**
	 * Establece el {@link Player} para capturar los eventos de este jugador.
	 * Se crea la referencia {@code ManagedReference} al jugador invocando al
	 * método {@code createReference()} del {@code DataManager}.
	 * 
	 * @param player instancia de un jugador.
	 */
	public void setPlayer(final Player player) {
		if (player == null) {
			throw new NullPointerException(
					"No existe una instancia para la clase Player");
		}
		DataManager dataMgr = AppContext.getDataManager();
		try {
			this.playerRef = dataMgr.createReference(player);
			LOGGER.log(
					Level.INFO, "Establecer una referencia al Player: {0} ",
					player.getIdEntity()
			);
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}

	/**  
	 * Crea un {@link IMessage} con el mensaje recibido y ademas se obtiene un 
	 * {@link ServerMsgProcessor} para el tipo de mensaje corresponiente. Luego
	 * se realiza el procesamiento del mismo.
	 *  
	 * @param msg mensaje que se recibe del cliente.
	 * 
	 * TODO review code fix.
	 */
	public void receivedMessage(final ByteBuffer msg) {
		try {
			IMessage iMessage = MessageFactory.getInstance().createMessage(msg);

			ServerMsgProcessor processor = 
				(ServerMsgProcessor) ServerMsgProssesorFactory.getInstance()
				.createProcessor(iMessage.getType());

			LOGGER.info("Llego mensaje directo al servidor tipo " 
					+ iMessage.getType());
			
			//obtener el mundo actual del jugador
			IGridStructure structure = GridManager.getInstance()
				.getStructure(getPlayer().getActualWorld());
			
			//obtener la celda donde se encuentra el jugador
			Cell cell = structure.getCell(getPlayer().getPosition());

			// inicialización del player
			processor.setPlayerAssociated(getPlayer());

			processor.setCellAssociated(cell);
			
			// ejecutar el procesador/tarea asociado/a al mensaje recivido.
			processor.process(iMessage);
			
		} catch (MalformedMessageException e) {
			LOGGER.log(Level.WARNING, "Exception: {0}", e);
		} catch (UnsopportedMessageException e) {
			LOGGER.log(Level.WARNING, "Exception: {0}", e);
		}
	}

	/**
	 * Desconecta al jugador de la sesión que tiene establecida con el 
	 * servidor. 
	 * 
	 * @param graceful si {@code true}, el cliente se desconecta
	 *        correctamente.
	 */
	public void disconnected(final boolean graceful) {
		Player player = getPlayer();
		ClientSession session = player.getSession();

		String grace = null; 
		if (graceful) {
			grace = "correctamente";
		} else {
			grace = "forzadamente";
		}

		LOGGER.log(
				Level.INFO,
				"El usuario {0} se ha desconectado {1}",
				new Object[] { session.getName(), grace } 
		);

		//salir del mundo actual
		exitWorld(player);
		
		//la sesión es inutilizada para un usuario desconectado. 
		player.setSession(null);

	}
	
	/**
	 * Este metodo se encarga de desuscribir el jugador del canal al que
	 * esta asociado y enviar el mensaje {@link MsgLeft} a los jugadores
	 * que estan en las celdas adyacentes.
	 * 
	 * @param player jugador que sale del mundo
	 */
	public void exitWorld(final Player player) {
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

		//obtener el mundo actual del jugador
		IGridStructure structure = GridManager.getInstance()
			.getStructure(player.getActualWorld());
		
		//obtener la celda donde se encuentra el jugador
		Cell cell = structure.getCell(player.getPosition());
		
		// obtener la sesion del jugador
		ClientSession session = player.getSession();
		
		// notificar a la celda actual que la entidad dinamica ha salido
		cell.send(msgLeft, session);
		
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
		//desuscribir al jugador de la celda del viejo mundo
		cell.leaveFromChannel(session);
	}
}
