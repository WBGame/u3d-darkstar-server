package ar.com.cubenet.server.leasson2;

import java.io.Serializable;
import java.util.Properties;
import java.util.logging.Logger;

import com.sun.sgs.app.AppContext;
import com.sun.sgs.app.AppListener;
import com.sun.sgs.app.ClientSession;
import com.sun.sgs.app.ClientSessionListener;
import com.sun.sgs.app.DataManager;

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
	private static final Logger logger = Logger.getLogger( UserProxy.class.getName() );	
	
	/**
	 * Inicialización del sistema. 
	 */
	public void initialize(Properties props) {
		logger.info( "Inicialización del sistema por primera vez." );
	}

	/**
	 * Metodo que será llamando cuando el usuario comienze con el login en 
	 * sistema. 
	 */
	public ClientSessionListener loggedIn(ClientSession session) {
		logger.info( "Login de un cliente." );

		// Data manager del sistema
		DataManager d = AppContext.getDataManager();
		
		// Retorno del metodo
		UserListener user;
		
		// ver las excepsiones del metodo getBinding
		try {
			
			logger.info( "Intentando recuperar una instancia del Object " +
					"Store para " + session.getName() );
			
			// recupero del object store el objecto que necesito
			user = (UserListener) d.getBinding( session.getName() );
			
		} catch (Exception e) {
			logger.info( "No existe ninguna instancia dentro del Object " +
					"Store para " + session.getName() );
			
			user = new UserListener();
			
			// registro el objeto dentro del Object Store.
			d.setBinding( session.getName() , user );
		}
		
		user.setSession( session );
		
		return user;
	}
}
