package ar.edu.unicen.exa.server.grid;

import java.nio.ByteBuffer;

import ar.edu.unicen.exa.server.communication.processors.ServerMsgProcessor;
import ar.edu.unicen.exa.server.player.Player;

import com.sun.sgs.app.AppContext;
import com.sun.sgs.app.Channel;
import com.sun.sgs.app.ChannelListener;
import com.sun.sgs.app.ClientSession;
import common.messages.IMessage;
import common.messages.MessageFactory;
import common.processors.MsgProcessorFactory;

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
public class ChannelMessageListener implements ChannelListener {

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
				(ServerMsgProcessor) MsgProcessorFactory.getInstance()
				.createProcessor(iMessage.getType());
		
			Player p = (Player) AppContext.getDataManager().getBinding(
					session.getName() 
			);

			processor.setPlayerAsociete(p);
			
			//debo obtener la IGridStructure del jugador
			IGridStructure structure = GridManager.getInstance()
					.getStructure(p.getActualWorld());

			//Obtengo la celda donde está el jugador
			Cell current = structure.getCell(p.getPosition());
			if (current == null) {
				//throw el jugador está afuera del tablero!
				System.err.println("El jugador está afuera del tablero!");
			}
			
			processor.setCellAsociete(current);
			
			processor.process(iMessage);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
