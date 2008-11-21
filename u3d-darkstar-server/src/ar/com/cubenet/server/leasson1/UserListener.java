package ar.com.cubenet.server.leasson1;

import java.io.Serializable;
import java.nio.ByteBuffer;

import com.sun.sgs.app.AppContext;
import com.sun.sgs.app.ClientSession;
import com.sun.sgs.app.ClientSessionListener;
import com.sun.sgs.app.ManagedReference;

import java.util.logging.Logger;

/**
 * Una vez que pase el login esta clase será la que reciba la información que 
 * envía el usuario.
 */
public class UserListener implements Serializable, ClientSessionListener {

	private static final long serialVersionUID = 1L;

	/** The session this is listening to. */
	private final ManagedReference<ClientSession> sessionRef;

	/** Creamos un logger para esta clase */
	private static final Logger logger =
		Logger.getLogger(UserListener.class.getName());

	/** Estoy iniciando una conexion a un usuario dentro del sistema. */
	public UserListener(ClientSession session) {
		logger.info("Iniciando UserListener para " + session.getName());

		// mantengo una referencia al cliente que establecio la conexion.
		sessionRef = AppContext.getDataManager().createReference(session);
	}

	public void disconnected(boolean graceful) {
		logger.info("El usuario " + this.sessionRef.get().getName() + 
		" se a desconectado");
	}

	public void receivedMessage(ByteBuffer message) {
		// muestro el mensaje en la consola del servidor
		logger.info("Resiviendo mensaje de usuario " +
				this.sessionRef.get().getName() + " - " + 
				Transcoder.decodeString(message)
		);

		// cuando uso el buffer necesito rebobinarlo para que cuando lo envio
		// el cliente reciba toda la información, sino no le llega todo.
		message.rewind();

		// se lo reenvio al cliente
		sessionRef.get().send(message);
	}
}
