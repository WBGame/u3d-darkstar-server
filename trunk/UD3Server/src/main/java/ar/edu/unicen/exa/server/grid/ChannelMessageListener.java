/**
 * 
 */
package server.grid;

import com.sun.sgs.app.ChannelListener;
import server.communication.processors.ServerMsgProcessor;
import server.communication.tasks.TaskCommFactory;
import com.sun.sgs.app.Channel;
import com.sun.sgs.app.ClientSession;
import java.nio.ByteBuffer;

/** 
 *  Procesa los mensajes que llegan por los canales. Para esto debe generar un  {@code IMessage} dado los datos recibidos y generar un  {@code IProcessor} que procese el mensaje. No necesariamente debe haber un Listener para cada Channel, por ejemplo se puede asociar el mismo ChannelMessageListener a todos los canales.
 * @generated "De UML a Java V5.0 (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public class ChannelMessageListener implements ChannelListener {
	/**
	 * @param channel
	 * @param session
	 * @param msg
	 * @generated "De UML a Java V5.0 (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void receivedMessage(Channel channel, ClientSession session,
			ByteBuffer msg) {
		// begin-user-code
		// TODO Apéndice de método generado automáticamente

		// end-user-code
	}
}