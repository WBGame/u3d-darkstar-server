package ar.edu.unicen.exa.server;

import ar.edu.unicen.exa.server.communication.processors.ServerMsgProcessor;

import ar.edu.unicen.exa.server.grid.GridManager;
import ar.edu.unicen.exa.server.grid.IGridStructure;
import ar.edu.unicen.exa.server.grid.MatrixGridStructure;
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
 * el jugador para la debida sesión.
 *
 * @author Pablo Inchausti <inchausti.pablo at gmail dot com/>
 * @encoding UTF-8
 * 
 */
public final class AppListenerImpl implements AppListener, Serializable {
	/** Para cumplir con la clase serializable. */
	private static final long serialVersionUID = 1L;

    /** Ancho de la grilla. */
	private static final int GRID_WIDTH = 10;
	
	/** Alto de la grilla. */
	private static final int GRID_HEIGHT	= 10;
	
	/** Tamaño de la celda. Cuadrada (100x100). */
	private static final int CELL_SIZE = 100;

	/** Posición original X del primer mundo. */
	private static final float SPAWN_POSITION_WORLD1_X = 100;
	/** Posición original Y del primer mundo. */
	private static final float SPAWN_POSITION_WORLD1_Y = 0;
	/** Posición original Z del primer mundo. */
	private static final float SPAWN_POSITION_WORLD1_Z = 100;
	/** Posición original X del primer mundo. */
	private static final float SPAWN_POSITION_WORLD2_X = 150;
	/** Posición original Y del primer mundo. */
	private static final float SPAWN_POSITION_WORLD2_Y = 0;
	/** Posición original Z del primer mundo. */
	private static final float SPAWN_POSITION_WORLD2_Z = 150;
	
	/**
	 * Este metodo es invocado cuando el servidor se ejecuta por primera 
	 * vez. Crea los mundos en los cuales una entidad dinamica pueda 
	 * interactuar y se agregan al <Link GridManager/>. Finalmente se 
	 * configura la fabrica de procesadores para el manejo de los mensajes
	 * en el servidor.
	 *  
	 * @param props propiedades para configurar la aplicacion
	 */
	public void initialize(final Properties props) {
		
		//obtener la instancia de GridManager
		GridManager gridManager = GridManager.getInstance();

		
		//crear la grilla a partir de los datos definidos anteriormente
		IGridStructure world1 = new MatrixGridStructure(
				GRID_WIDTH, GRID_HEIGHT, CELL_SIZE);
		//definir cual será la posicion inicial del jugador dentro del mundo
		world1.setSpawnPosition(
				SPAWN_POSITION_WORLD1_X,
				SPAWN_POSITION_WORLD1_Y,
				SPAWN_POSITION_WORLD1_Z
			);
		//agregamos el nuevo mundo al GridManager
		gridManager.addStructure(world1);
		
		//creacion del segundo mundo con las mismas caracteristicas que el
		//primero, salvo la posicion inicial del jugador dentro de este mundo
		IGridStructure world2 = new MatrixGridStructure(
				GRID_WIDTH, GRID_HEIGHT, CELL_SIZE);
		world2.setSpawnPosition(
				SPAWN_POSITION_WORLD2_X,
				SPAWN_POSITION_WORLD2_Y,
				SPAWN_POSITION_WORLD2_Z
			);
		gridManager.addStructure(world2);
		
		//configuracion de la factoria de procesadores para los mesajes del
        //correspondientes al sevidor
        ServerMsgProcessor.configureMsgProcessorFactory();
	}

	/** 
	 * Este método es invocado cada vez que un usuario se logea en el sistema.
	 *  
	 * Por medio de la sesion se obtiene el Player. En caso de que sea null,
	 * significa que existe un jugador con el mismo nobre de usuario conectado
	 * en el sistema, lo que se rechazara el acceso al usuario.
	 * Si pasa esta verificacion, se crea se crea un UserSessionListener para 
	 * capurar los eventos asociados al jugador y se setea al Player la sesion
	 * actual. Finalmente se suscribe el jugador al canal por defecto y se 
	 * retorna una instancia de UserSessionListener.
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
