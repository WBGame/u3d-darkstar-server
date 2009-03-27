package ar.edu.unicen.exa.server.player;

import java.io.Serializable;
import java.nio.ByteBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;

import ar.edu.unicen.exa.server.communication.processors.ServerMsgProcessor;
import ar.edu.unicen.exa.server.communication
		.processors.ServerMsgProssesorFactory;
import ar.edu.unicen.exa.server.grid.Cell;
import ar.edu.unicen.exa.server.grid.GridManager;
import ar.edu.unicen.exa.server.grid.IGridStructure;

import com.sun.sgs.app.AppContext;
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
 * Esta asociado uno a uno con cada {@link Player} del sistema. 
 * Debe atender peticiones de desconexión y mensajes entrantes.
 * 
 * @author Pablo Inchausti <inchausti.pablo at gmail dot com/> 
 * @encoding UTF-8 
 */
public final class UserSessionListener 
implements ClientSessionListener, Serializable {

	/**  Para cumplir con la version de la clase Serializable. */
	private static final long serialVersionUID = 4348700548351927735L;

	/** Logger para esta clase. */
	private static final Logger LOGGER = 
		Logger.getLogger(UserSessionListener.class.getName());

	/**
	 * Es una referencia {@code ManagedReference} al {@link Player}.
	 */
	private ManagedReference<Player> playerRef;

	/**
	 * Retorna un {@link Player} a partir de la referencia 
	 * {@code ManagedReference} al {@link Player} asociado.
	 * 
	 * @return Player jugador que hace referencia el {@link ManagedReference}.
	 */
	public Player getPlayer() {
		return playerRef.get();
	}

	/**
	 * Establece el {@link Player} para capturar los eventos de este 
	 * {@link Player}.
	 * Se crea la referencia {@code ManagedReference} al jugador invocando al
	 * método {@code createReference()} del {@code DataManager}.
	 * 
	 * @param player instancia de un {@link Player}.
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
	 */
	public void receivedMessage(final ByteBuffer msg) {
		try {
			IMessage iMessage = MessageFactory.getInstance().createMessage(msg);

			ServerMsgProcessor processor = 
				(ServerMsgProcessor) ServerMsgProssesorFactory.getInstance()
				.createProcessor(iMessage.getType());

			LOGGER.info("Llego mensaje directo al servidor tipo " 
					+ iMessage.getType());

			// Obtener el mundo actual del jugador.
			IGridStructure structure = GridManager.getInstance()
			.getStructure(getPlayer().getActualWorld());

			// Obtener la celda donde se encuentra el jugador.
			Cell cell = structure.getCell(getPlayer().getPosition());

			// Inicialización del player.
			processor.setPlayerAssociated(getPlayer());
			processor.setCellAssociated(cell);

			// Ejecutar el procesador/tarea asociado/a al mensaje recibido.
			processor.process(iMessage);
		} catch (MalformedMessageException e) {
			LOGGER.log(Level.WARNING, "Exception: {0}", e);
		} catch (UnsopportedMessageException e) {
			LOGGER.log(Level.WARNING, "Exception: {0}", e);
		} catch (Exception e) { 
			e.printStackTrace();
		}
	}

	/**
	 * Desconecta al {@link Player} de la sesión que tiene establecida con el 
	 * servidor. 
	 * ClientSession a partir de este momento es null porque el usuario ya no 
	 * está logueado en el sistema.
	 * 
	 * @param graceful {@code true} si el usuario se desconecto utilizando el 
	 *        método logout presente en el cliente. {@code false} en otro caso.
	 * 
	 * @see {@linkplain SimpleClient#logout(boolean) }
	 */
	public void disconnected(final boolean graceful) {
		Player player = getPlayer();

		// La sesion es inutilizada para un usuario desconectado. 
		player.setSession(null);

		String grace = null; 
		if (graceful) {
			grace = "correctamente";
		} else {
			grace = "forzadamente";
		}

		LOGGER.log(
				Level.INFO,
				"El usuario {0} se ha desconectado {1}",
				new Object[] { player.getIdEntity() , grace } 
		);

		// Salir del mundo actual.
		exitWorld(player);
	}

	/**
	 * Este método se encarga de desuscribir el {@link Player} del canal al que
	 * esta asociado y enviar el mensaje {@link MsgLeft} a los {@link Player}s
	 * que estan en las celdas adyacentes.
	 * 
	 * @param player {@link Player} que sale del mundo.
	 */
	public void exitWorld(final Player player) {
		// Crear el mensaje de partida del jugador.
		IMessage msgLeft = null;
		try {
			msgLeft = MessageFactory.getInstance().createMessage(
					MsgTypes.MSG_LEFT_TYPE);
			((MsgPlainText) msgLeft).setMsg(player.getIdEntity());
		} catch (UnsopportedMessageException e1) {
			e1.printStackTrace();
		}

		// Obtener el mundo actual del jugador.
		IGridStructure structure = 
			GridManager.getInstance().getStructure(player.getActualWorld());

		// Obtener la celda donde se encuentra el jugador.
		Cell cell = structure.getCell(player.getPosition());

		// Como el player se a desconectado player.getSession() retorna null, lo
		// cual es totalmente correcto, ya que el usuario al desconectarse deja
		// de ser válido y debe utilizarse null para poder notificar a los 
		// restantes players que el player en cuestion ya no esta en la celda.
		LOGGER.log(
				Level.INFO,
				"El usuario {0} abandona la celda {1}",
				new Object[] { player.getIdEntity() , cell.getId() } 
		);
		cell.send(msgLeft, player.getSession());

		// Notificar a las celdas adyacentes que el jugador no se encuentra en
		// la celda.
		Cell[] adyacentes = structure.getAdjacents(cell, player.getPosition());
		if (adyacentes != null) {
			for (int i = 0; i < adyacentes.length; i++) {
				/* Se manda null como ClientSession (ver javadoc 
				 * de {@link Channel#send(ClientSession, ByteBuffer)})
				 */
				adyacentes[i].send(msgLeft, null);
			}
		} 

		// Desuscribir al jugador de la celda del viejo mundo.
		cell.leaveFromChannel(player.getSession());
	}
}
