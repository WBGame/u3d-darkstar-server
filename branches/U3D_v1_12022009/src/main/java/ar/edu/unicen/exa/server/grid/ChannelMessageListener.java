package ar.edu.unicen.exa.server.grid;

import java.io.Serializable;
import java.nio.ByteBuffer;

import java.util.logging.Logger;

import ar.edu.unicen.exa.server.communication.processors.ServerMsgProcessor;
import ar.edu.unicen.exa.server.communication.processors
         .ServerMsgProssesorFactory;
import ar.edu.unicen.exa.server.player.Player;

import com.sun.sgs.app.AppContext;
import com.sun.sgs.app.Channel;
import com.sun.sgs.app.ChannelListener;
import com.sun.sgs.app.ClientSession;
import com.sun.sgs.app.DataManager;
import common.exceptions.MalformedMessageException;
import common.exceptions.UnsopportedMessageException;
import common.messages.IMessage;
import common.messages.MessageFactory;

/**
 * Procesa los mensajes que llegan por los canales. Para esto debe generar un
 * {@code IMessage} dado los datos recibidos y generar un {@code IProcessor} que
 * procese el mensaje. No necesariamente debe haber un Listener para cada
 * Channel, por ejemplo se puede asociar el mismo ChannelMessageListener a todos
 * los canales.
 * 
 * @encoding UTF-8.
 */
public class ChannelMessageListener implements ChannelListener, Serializable {

	/**  Para cumplir con la version de la clase {@Serializable}. */
	private static final long serialVersionUID = 1L;

	/** Logger. */
	private static Logger logger = 
		Logger.getLogger(ChannelMessageListener.class.getName());

	/**
	 * 
	 * <ol>
     * <li>Regenera el mensaje.</li>
     * <li>Crea el procesador asociado al mismo.</li>
     * <li>Recupera el {@link Player}.</li> 
     * <li>Ejecuta el proceso asociado al mensaje.</li> 
     * </ol>
	 * {@inheritDoc}
	 * 
	 * @param channel
	 *            Un {@link Channel}.
	 * @param session
	 *            Envio de {@link ClientSession}.
	 * @param msg
	 *            Un mensaje.
	 */
	public final void receivedMessage(final Channel channel,
			final ClientSession session, final ByteBuffer msg) {
		try {
			// Regenerar el objeto mensaje
			IMessage iMessage = MessageFactory.getInstance().createMessage(msg);

			// Crear el procesador asociado al mismo.
			ServerMsgProcessor processor = 
				(ServerMsgProcessor) ServerMsgProssesorFactory.getInstance()
				.createProcessor(iMessage.getType());

			logger.info("Llego mensaje en Channel " + channel.getName() 
					+ ", tipo " + iMessage.getType());
			
			DataManager dataMgr = AppContext.getDataManager();

			// Recuperar el jugador desde el DataManager.
			Player player = (Player) dataMgr.getBinding(
					session.getName() 
			);

			// Obtener el mundo actual del jugador
			IGridStructure structure = GridManager.getInstance()
			.getStructure(player.getActualWorld());

			// Obtener la celda donde se encuentra el jugador
			Cell cell = structure.getCell(player.getPosition());

			// Inicialización para ejecutar el proceso asociado al mensaje.
			processor.setPlayerAssociated(player);
			processor.setCellAssociated(cell);

			// Ejecución del procesamiento.
			processor.process(iMessage);
		} catch (MalformedMessageException e) {
			logger.warning("Mensaje inconsistente.");		
			e.printStackTrace();
		} catch (UnsopportedMessageException e) {
			logger.warning("Mensaje inexistente.");
			e.printStackTrace();
		}		
	}
}
