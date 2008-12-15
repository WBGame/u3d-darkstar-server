package ar.edu.unicen.exa.server.grid;

import java.nio.ByteBuffer;

import ar.edu.unicen.exa.server.communication.tasks.TaskCommFactory;
import ar.edu.unicen.exa.server.communication.tasks.TaskCommunication;
import ar.edu.unicen.exa.server.player.Player;

import com.sun.sgs.app.AppContext;
import com.sun.sgs.app.Channel;
import com.sun.sgs.app.ChannelListener;
import com.sun.sgs.app.ClientSession;
import common.messages.IMessage;
import common.messages.MessageFactory;

/**
 * Procesa los mensajes que llegan por los canales. Para esto debe generar un
 * {@code IMessage} dado los datos recibidos y generar un {@code IProcessor} que
 * procese el mensaje. No necesariamente debe haber un Listener para cada
 * Channel, por ejemplo se puede asociar el mismo ChannelMessageListener a todos
 * los canales.
 * 
 * @author Jair Tabares.
 * @encoding UTF-8
 * 
 * TODO probably is needed set player cell to.
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
			TaskCommunication taskCommunication = TaskCommFactory.getInstance()
					.createComTask(iMessage);
			
			Player p = (Player) AppContext.getDataManager().getBinding(
					session.getName() 
			);
			
			taskCommunication.setPlayerAsociete(p);
			
			AppContext.getTaskManager().scheduleTask(taskCommunication);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
