package ar.com.cubenet.server.leasson1;

import java.io.Serializable;
import java.util.Properties;
import java.util.logging.Logger;

import com.sun.sgs.app.AppListener;
import com.sun.sgs.app.ClientSession;
import com.sun.sgs.app.ClientSessionListener;

/**
 * Inicialización del server y manejo del login del usuarios
 * previo.
 * 
 * AppListener es un derivado de ManagedObject asi que necesito implementar
 * Serializable.
 */
public class UserProxy implements AppListener, Serializable {

	/** Para cumplir con la clase serializable. */
	private static final long serialVersionUID = 1L;

	/** Creamos un logger para esta clase */
	private static final Logger logger = Logger.getLogger(UserProxy.class.getName());	
	
	/**
	 * Inicialización del sistema. 
	 */
	public void initialize(Properties props) {
		logger.info("Inicialización del sistema por primera vez.");
	}

	/**
	 * Metodo que será llamando cuando el usuario comienze con el login en 
	 * sistema. 
	 * 
	 * @param session asdasds
	 */
	public ClientSessionListener loggedIn(ClientSession session) {
		// Return a valid listener
		logger.info("Login de un cliente.");
		return new UserListener(session);
	}

}
