package ar.edu.unicen.exa.server;

import ar.edu.unicen.exa.server.grid.Cell;
import ar.edu.unicen.exa.server.grid.GridManager;
import ar.edu.unicen.exa.server.grid.IGridStructure;
import ar.edu.unicen.exa.server.grid.MatrixGridStructure;
import ar.edu.unicen.exa.server.player.Player;
import ar.edu.unicen.exa.server.player.UserSessionListener;


import com.jme.math.Vector3f;
import com.sun.sgs.app.AppListener;
import java.io.Serializable;
import java.util.Properties;
import java.util.logging.Logger;

import com.sun.sgs.app.ClientSessionListener;
import com.sun.sgs.app.ClientSession;
import common.exceptions.UnsopportedMessageException;
import common.messages.IMessage;
import common.messages.MessageFactory;
import common.messages.MsgPlainText;
import common.messages.MsgTypes;

/**
 * Esta clase captura los eventos correspondiente al loggedIn de un usuario.
 * Basicamente se encarga de inicializar el servidor y recuperar o crear
 * el jugador para la debida sesi�n.
 *
 * @author Pablo Inchausti &lt;inchausti.pablo at gmail dot com&gt;
 * @encoding UTF-8
 */
public final class AppListenerImpl implements AppListener, Serializable {
	
	/** Para cumplir con la clase serializable. */
	private static final long serialVersionUID = 1L;

	/** Logger. */
	private static Logger logger = 
		Logger.getLogger(AppListenerImpl.class.getName());
	
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
	
	/** Posición original X del segundo mundo. */
	private static final float SPAWN_POSITION_WORLD2_X = 150;
	/** Posición original Y del segundo mundo. */
	private static final float SPAWN_POSITION_WORLD2_Y = 0;
	/** Posición original Z del segundo mundo. */
	private static final float SPAWN_POSITION_WORLD2_Z = 150;
	
	/**
	 * Este m�todo es invocado cuando el servidor se ejecuta por primera 
	 * vez. Crea los mundos en los cuales una entidad dinamica pueda 
	 * interactuar y se agregan al {@link GridManager}. Finalmente se 
	 * configura la fabrica de procesadores para el manejo de los mensajes
	 * en el servidor.
	 *  
	 * @param props propiedades para configurar la aplicacion
	 */
	public void initialize(final Properties props) {
		logger.info("initialize server state");
		// obtener la instancia de GridManager
		GridManager gridManager = GridManager.getInstance();
		
		
		//crear la grilla a partir de los datos definidos anteriormente
		IGridStructure world1 = new MatrixGridStructure(
				GRID_WIDTH, GRID_HEIGHT, CELL_SIZE);
		//definir cual ser� la posicion inicial del jugador dentro del mundo
		world1.setSpawnPosition(
				SPAWN_POSITION_WORLD1_X,
				SPAWN_POSITION_WORLD1_Y,
				SPAWN_POSITION_WORLD1_Z
			);
		//agregamos el nuevo mundo al GridManager
		gridManager.addStructure(world1);
		
		//creación del segundo mundo con las mismas caracteristicas que el
		//primero, salvo la posicion inicial del jugador dentro de este mundo
		IGridStructure world2 = new MatrixGridStructure(
				GRID_WIDTH, GRID_HEIGHT, CELL_SIZE);
		world2.setSpawnPosition(
				SPAWN_POSITION_WORLD2_X,
				SPAWN_POSITION_WORLD2_Y,
				SPAWN_POSITION_WORLD2_Z
			);
		gridManager.addStructure(world2);

	}

	/** 
	 * Este m�todo es invocado cada vez que un usuario se logea en el sistema.
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
			//ingresar el jugador al mundo
			enterWorld(player);
			return user;
		}
		return null;
	}
	
	/**
	 * Este metodo ingresa al jugador al mundo por defecto. ingreso de un 
	 * jugador a un mundo. Se obtiene el mundo por medio del mensaje recivido 
	 * el mundo al que desea ingresar, actualizando el mundo del jugador como
	 * asi tambien la celda y posicion inicial, suscribirlo al canal y 
	 * finalmente enviar el mensaje {@link MsgArrived} a las celdas 
	 * correspondientes. 
	 *  
	 * @param player jugador que ingresa al mundo  
	 */
	public void enterWorld(final Player player) {
		//obtener el mundo por defecto
		IGridStructure structure = GridManager.getInstance()
			.getDefaultStructure();
		// actualizar el jugador con el id del mundo
		player.setActualWorld(structure.getIdWorld());
		//definicion del angulo por defecto
		player.setAngle(new Vector3f(1, 1, 1));
		//establecer la posicion inicial del jugador dentro del mundo
		player.setPosition(structure.getSpawnPosition());
		// obtener la celda por defecto a partir del nuevo mundo
		Cell cell = structure.getSpawnCell();
		//obtener la sesion del jugador
		ClientSession session = player.getSession();
		// suscribir al jugador a la nueva celda
		cell.joinToChannel(session);
		//crear el mensaje de ingreso al mundo por defecto
		IMessage msgArrived = null;
		try {
			msgArrived = MessageFactory.getInstance()
				.createMessage(MsgTypes.MSG_ARRIVED_TYPE);
			//seteo el mensaje con el id del jugador
			((MsgPlainText) msgArrived).setMsg(player.getIdEntity());
		} catch (UnsopportedMessageException e) {
			e.printStackTrace();
		}
		//notificar a la celda por defecto que ingresó el jugador
		cell.send(msgArrived, session);
		//obtener los adyacentes de la nueva celda
		Cell[] adyacentes = structure
			.getAdjacents(cell, player.getPosition());
		//notificar a las celdas adyacentes que ingresó el jugador
		if (adyacentes != null) {
			for (int i = 0; i < adyacentes.length; i++) {
				adyacentes[i].send(msgArrived, session);
			}
		}
	}
}
