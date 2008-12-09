package ar.edu.unicen.exa.server;

import ar.edu.unicen.exa.server.player.Player;
import ar.edu.unicen.exa.server.player.UserSessionListener;

import com.sun.sgs.app.AppListener;
import java.io.Serializable;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.sun.sgs.app.AppContext;
import com.sun.sgs.app.ClientSessionListener;
import com.sun.sgs.app.ClientSession;
import com.sun.sgs.app.DataManager;

/**
 * Escucha los eventos de aplicación.  Esta clase es invocada cuando la 
 * aplicación se ejecuta por primera vez o los clientes se logean en el 
 * sistema.
 * Basicamente se encarga de inicializar el servidor y recuperar o crear
 * el jugardor para una sesion.
 *
 * @author Pablo Inchausti <inchausti.pablo at gmail dot com>
 * @encoding UTF-8
 * 
 * @todo method initialize doesn't have the final implementation.
 * @todo method loggedIn need more delegation to class Player.
 */
public class AppListenerImpl implements AppListener, Serializable {
	/** Para cumplir con la clase serializable. */
	private static final long serialVersionUID = 1L;

	/** Creamos un logger para esta clase. */
	private static final Logger logger =
		Logger.getLogger(AppListenerImpl.class.getName());

	/**
	 * Este metodo es invocado cuando el servidor es ejecutado por primera 
	 * vez. No tiene codigo asociado solo imprime que el servidor se inicio
	 * correctamente.  
	 *
	 * @param props propiedades para configurar la aplicacion
	 */
	public void initialize(Properties props) {
		logger.info("Servidor Inicializado");
	}

	/** 
	 * Este metodo es invocado cuando cualquier usuario previa autenticacion,
	 * se logea en el sistema. 
	 * Si el usuario/jugador entra al sistema por primera vez, se producira
	 * una excepcion debido a que se quiere recuperar la informacion 
	 * correspondiente a un jugador que no se encuentra almacenado en el 
	 * Object Store. Esta excepcion es capturada mediante la sentencia 
	 * try-catch para que se cree el nuevo jugador seteando su ID de 
	 * identidad (el nombre de la sesion) y almacenandolo {@link #setBinding()} 
	 * con el binding ID de identidad para posterior recuperación. 
	 * Por otro lado, si ya se ha registrado en el Object Store, entonces no
	 * se producirá la excepcion y se obtiene el jugador por medio del 
	 * DataManager utilizando el método {@link #getBinding()).
	 * Finalmente, se crea un UserSessionListener asociándolo al jugador.
	 * 
	 * @see AppListener#loggedIn(ClientSession session)
	 * @param session sesion de un player
	 * @return ClientSessionListener un listener para la session del jugador.
	 */
	public ClientSessionListener loggedIn(ClientSession session) {

		// Data manager del sistema
		DataManager d = AppContext.getDataManager();

		// Retorno del metodo
		UserSessionListener user;

		// Jugador
		Player player;

		try {
			logger.info(
					"Intentando recuperar una instancia del Object "
					+ "Store para " + session.getName()
			);
			player = (Player) d.getBinding(session.getName());
		} catch (Exception e) {

			logger.info(
					"No existe ninguna instancia dentro del Object "
					+ "Store para " + session.getName()
			);

			player = new Player();

			player.setIdEntity(session.getName());

			logger.log(
					Level.INFO, "Id Identity Player: {0}", 
					player.getIdEntity()
			);

			// registro el objeto dentro del Object Store.
			d.setBinding(player.getIdEntity() , player);

			logger.info( "Nombre de identificacion: " + player.getIdEntity() );
		}

		user = new UserSessionListener();
		player.setSession(session);
		user.setPlayer(player);

		logger.log(
				Level.INFO, 
				"Usuario {0} ha sido logeado en el Servidor" ,
				session.getName()
		);

		return user;
	}
}