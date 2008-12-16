package ar.edu.unicen.exa.server;

import ar.edu.unicen.exa.server.player.Player;
import ar.edu.unicen.exa.server.player.UserSessionListener;

import com.sun.sgs.app.AppListener;
import java.io.Serializable;
import java.util.Properties;

import com.sun.sgs.app.ClientSessionListener;
import com.sun.sgs.app.ClientSession;

/**
 * Esta clase captura los eventos correspondiente al loggedIn de un usuario.
 * Basicamente se encarga de inicializar el servidor y recuperar o crear
 * el jugardor para la debida sesión.
 *
 * @author Pablo Inchausti <inchausti.pablo at gmail dot com/>
 * @encoding UTF-8
 * 
 * TODO method initialize doesn't have the final implementation.
 * TODO method loggedIn need more delegation to class Player.
 */
public final class AppListenerImpl implements AppListener, Serializable {
	/** Para cumplir con la clase serializable. */
	private static final long serialVersionUID = 1L;

	/**
	 * Este metodo es invocado cuando el servidor es ejecutado por primera 
	 * vez. No tiene código asociado solo imprime que el servidor se inicio
	 * correctamente.  
	 * 
	 * TODO faltaría inicializar todo lo necesaio para poder persistir
	 * la información que maneja el servidor, ej {@link MatrixGridStructure}.  
	 * 
	 * @param props propiedades para configurar la aplicacion
	 */
	public void initialize(final Properties props) { }

	/** 
	 * Este método es invocado cada vez que un usuario se logea en el sistema.
	 *  
	 * Por medio de la sesion se obtiene el Player. En caso de que sea null,
	 * significa que existe un jugador con el mismo nobre de usuario conectado
	 * en el sistema, lo que se rechazara el acceso al usuario.
	 * Si pasa esta verificacion, se crea se crea un UserSessionListener para 
	 * capurar los eventos asociados al jugador y se setea al Player la sesion
	 * actual. Finalmente se retorna el UserSessionListener.
	 * 
	 * @see AppListener#loggedIn(ClientSession session)
	 * @param session sesion de un player
	 * @return ClientSessionListener un listener para la session del jugador.
	 */
	public ClientSessionListener loggedIn(final ClientSession session) {
		// Retorno del metodo
		UserSessionListener user;
		// Jugador
		Player player = Player.create(session);
		if (player != null) {
			// para capturar los eventos asociados a este jugador
			user = new UserSessionListener();
			// asigno la nueva sesión al jugador
			player.setSession(session);
			user.setPlayer(player);
			return user;
		}
		return null;
	}
}
