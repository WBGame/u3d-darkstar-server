package ar.edu.unicen.exa.server.grid;

import java.io.Serializable;
import java.nio.ByteBuffer;

import ar.edu.unicen.exa.server.communication.processors.ServerMsgProcessor;
import ar.edu.unicen.exa.server.communication.processors.ServerMsgProssesorFactory;
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
 * @encoding UTF-8
 * 
 */
//XXX debe implementar Serializable para que no se produzcan excepciones.

public class ChannelMessageListener implements ChannelListener, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * @param channel
	 *            a channel
	 * @param session
	 *            the sending client session
	 * @param msg
	 *            a message
	 */
	public final void receivedMessage(final Channel channel,
			final ClientSession session, final ByteBuffer msg) {
			
		try {
			IMessage iMessage = MessageFactory.getInstance().createMessage(msg);
			
			ServerMsgProcessor processor = 
				(ServerMsgProcessor) ServerMsgProssesorFactory.getInstance()
				.createProcessor(iMessage.getType());
		

			DataManager dataMgr = AppContext.getDataManager();
			
			//recuperar el jugador desde el DataManager
			Player player = (Player) dataMgr.getBinding(
					session.getName() 
			);
					
			//obtener lel mundo actual del jugador
			IGridStructure structure = GridManager.getInstance()
					.getStructure(player.getActualWorld());
			
			//obtener la celda donde se encuentra el jugador
			Cell cell = structure.getCell(player.getPosition());
		
			processor.setPlayerAssociated(player);
			
			processor.setCellAssociated(cell);
			
			processor.process(iMessage);
			
		} catch (MalformedMessageException e) {
					e.printStackTrace();
		} catch (UnsopportedMessageException e) {
					e.printStackTrace();
		}		
	}
}
