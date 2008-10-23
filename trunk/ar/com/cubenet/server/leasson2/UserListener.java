package ar.com.cubenet.server.leasson2;

import java.io.Serializable;
import java.nio.ByteBuffer;

import com.sun.sgs.app.AppContext;
import com.sun.sgs.app.ClientSession;
import com.sun.sgs.app.ClientSessionListener;
import com.sun.sgs.app.DataManager;
import com.sun.sgs.app.ManagedObject;
import com.sun.sgs.app.ManagedReference;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Una vez que pase el login esta clase será la que reciba la información que 
 * envía el usuario.
 * 
 * leasson2:
 * Ahora la clase que representa la conexion del usuario tambien es persistente
 * al agregar la interface ManagedObject.
 */
public class UserListener 
implements ManagedObject, Serializable, ClientSessionListener {

	private static final long serialVersionUID = 1L;

	/** The session this is listening to. */
	private ManagedReference<ClientSession> sessionRef;

	/** Creamos un logger para esta clase */
	private static final Logger logger =
		Logger.getLogger( UserListener.class.getName() );

	public void disconnected(boolean graceful) {
		logger.info( "El usuario " + this.sessionRef.get().getName() + 
		" se a desconectado" );
		
		setSession(null);
	}

	public void receivedMessage(ByteBuffer message) {
		// muestro el mensaje en la consola del servidor
		logger.info( "Resiviendo mensaje de usuario " + 
				this.sessionRef.get().getName() + " - " + 
				Transcoder.decodeString(message)
		);

		// cuando uso el buffer necesito rebobinarlo para que cuando lo envio
		// el cliente reciba toda la información, sino no le llega todo.
		message.rewind();

		// se lo reenvio al cliente
		sessionRef.get().send( message );
	}
	
	public ClientSession getSession() {
        if (sessionRef == null)
            return null;
		
		return sessionRef.get();
	}

    public void setSession(ClientSession session) {
        DataManager dataMgr = AppContext.getDataManager();
        dataMgr.markForUpdate(this);

        try {
        	sessionRef = dataMgr.createReference(session);	
		} catch (Exception e) {
			sessionRef = null;
		}

        logger.log(Level.INFO, "Set session for {0} to {1}", new Object[] { this, session });
    }
}
