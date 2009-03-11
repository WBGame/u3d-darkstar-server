package ar.edu.unicen.exa.server;

import java.io.Serializable;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import ar.edu.unicen.exa.server.grid.Cell;
import ar.edu.unicen.exa.server.grid.GridManager;
import ar.edu.unicen.exa.server.grid.IGridStructure;
import ar.edu.unicen.exa.server.player.Player;
import ar.edu.unicen.exa.server.player.UserSessionListener;

import com.jme.math.Vector3f;
import com.sun.sgs.app.AppListener;
import com.sun.sgs.app.ClientSession;
import com.sun.sgs.app.ClientSessionListener;
import common.exceptions.UnsopportedMessageException;
import common.messages.IMessage;
import common.messages.MessageFactory;
import common.messages.MsgPlainText;
import common.messages.MsgTypes;

/**
 * En esta clase se capturan los siguintes eventos :
 * <ol>
 * <li>Encarga de inicializar el servidor.</li>
 * <li>Recuperar o crear el {@link Player}.</li>
 * <li>Una vez que el usuario se logeo, captura los correspondientes 
 *     eventos.</li> 
 * </ol>
 * 
 * @author Pablo Inchausti &lt;inchausti.pablo at gmail dot com&gt;
 * @encoding UTF-8.
 */
public final class AppListenerImpl implements AppListener, Serializable {

	/** Para cumplir con la clase serializable. */
	private static final long serialVersionUID = 1L;

	/** Logger. */
	private static Logger logger = 
		Logger.getLogger(AppListenerImpl.class.getName());

	/**
	 * Este metodo es invocado cuando el servidor se ejecuta por primera 
	 * vez. Crea los mundos en los cuales una entidad dinamica pueda 
	 * interactuar y se agregan al {@link GridManager}. Finalmente se 
	 * configura la factory de procesadores para el manejo de los mensajes
	 * en el servidor.
	 *  
	 * @param props Propiedades para configurar la aplicacion.
	 */
	public void initialize(final Properties props) {
		logger.info("initialize server state");
		StructureProperties.initGridManager(props);
	}
	
	/** 
	 * Este metodo es invocado cada vez que un usuario se logea en el sistema.
	 *  
	 * Por medio de la sesion se obtiene el {@link Player}. 
	 * En caso de que sea null, significa que existe un {@link Player}, 
	 * con el mismo nombre de usuario que se acaba de logear en el sistema, 
	 * por lo tanto se rechazara el acceso al usuario.
	 * Si pasa esta verificacion, se crea se crea un 
	 * {@link UserSessionListener} para capurar los eventos asociados al 
	 * usuario y se setea al {@link Player} la sesion actual. 
	 * Finalmente se suscribe el jugador al canal por defecto y se 
	 * retorna una instancia de {@link UserSessionListener}.
	 * 
	 * @see AppListener#loggedIn(ClientSession session)
	 * @param session Sesion de un {@link Player}
	 * @return ClientSessionListener Un {@link ClientSession} para la sesion 
	 *         del {@link Player}.
	 */
	public ClientSessionListener loggedIn(final ClientSession session) {
		// Retorno del metodo.
		UserSessionListener user;
		// Jugador.
		Player player = Player.create(session);
		if (player != null) {
			// Para capturar los eventos asociados a este jugador.
			user = new UserSessionListener();
			// Asigno la nueva sesión al jugador.
			player.setSession(session);
			user.setPlayer(player);
				// Ingresar el jugador al mundo.
			enterWorld(player);
			return user;
		}
		return null;
	}

	/**
	 * En este metodo el {@link Player} ingresa a un mundo por defecto.
	 * Luego se coloco al {@link Player} en la posicion inicial y angulo por 
	 * defecto dendro del {@link IGridStructure} (mundo), suscribirlo al canal 
	 * y finalmente enviar el mensaje {@link MsgArrived} a las celdas 
	 * correspondientes.
	 *  
	 * @param player {@link Player} que ingresa al mundo.
	 * 
	 * TODO debe ser mas inteligente esto. Si tengo un estado almacenado debe
	 * reutilizarse el mismo.
	 * 
	 * TODO debe agregarse el tratamiento de excepsiones y ademas decidir que 
	 * hacer si no es posible suscribir al jugador al mundo.
	 */
	public void enterWorld(final Player player) {
		// Obtener el mundo por defecto.
		IGridStructure structure = GridManager.getInstance()
			.getDefaultStructure();
		
		// Actualizar el jugador con el id del mundo si no tiene mundo
		if (player.getActualWorld() == null) {
			player.setActualWorld(structure.getIdWorld());
		}

		// Definicion del angulo por defecto.
		if (player.getAngle() == null) {
			player.setAngle(new Vector3f(1, 1, 1));
		}

		// Establecer la posicion inicial del jugador dentro del mundo.
		if (player.getPosition() == null) {
			player.setPosition(structure.getSpawnPosition());
		}

		// Obtener la celda por defecto a partir del nuevo mundo.
		Cell cell = structure.getCell(player.getPosition());

		// Obtener la sesion del jugador.
		ClientSession session = player.getSession();

		// Crear el mensaje de ingreso al mundo por defecto.
		IMessage msgArrived = null;
		try {
			msgArrived = MessageFactory.getInstance()
				.createMessage(MsgTypes.MSG_ARRIVED_TYPE);

			// Seteo el mensaje con el id del jugador.
			((MsgPlainText) msgArrived).setMsg(player.getIdEntity());
		} catch (UnsopportedMessageException e) {
			logger.log(Level.SEVERE,
				"Fallo la creación del mensaje arrived. Abortando EnterWorld.",
				e);
			return;
		}

		// Notificar a la celda por defecto que ingreso el jugador.
		cell.send(msgArrived, null);

		// Obtener los adyacentes de la nueva celda.
		Cell[] adyacentes = structure
		.getAdjacents(cell, player.getPosition());
		// Notificar a las celdas adyacentes que ingreso el jugador.
		if (adyacentes != null) {
			for (int i = 0; i < adyacentes.length; i++) {
				adyacentes[i].send(msgArrived, null);
			}
		}
		logger.info(player.getIdEntity() + " -> entra a " 
				+ cell.getId() + " del mundo " + structure.getId());
		// Suscribir al jugador a la nueva celda.
		// Lo hacemos al final para que no le llegue el msg de arrived al player
		// que se esta conectando.
		cell.joinToChannel(session);
	}

}
