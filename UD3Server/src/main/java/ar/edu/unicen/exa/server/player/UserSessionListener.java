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

			// obtener el mundo actual del jugador
			String actualWorld = getPlayer().getActualWorld();

			// si estoy suscripto a algún mundo => recupero la celda para 
			// inicializar el proceso
			if (actualWorld != null) {
				IGridStructure world = GridManager.getInstance()
					.getStructure(getPlayer().getActualWorld());
				Cell cell = world.getCell(getPlayer().getPosition());
				processor.setCellAssociated(cell);
			}
			
			// inicialización del player
			processor.setPlayerAssociated(getPlayer());
			
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
		ClientSession session = getPlayer().getSession();

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

		// la sesión es inutilizada para un usuario desconectado. 
		getPlayer().setSession(null);
	}
}
