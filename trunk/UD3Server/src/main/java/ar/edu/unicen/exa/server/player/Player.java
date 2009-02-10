package ar.edu.unicen.exa.server.player;

import ar.edu.unicen.exa.server.entity.DynamicEntity;
import com.sun.sgs.app.AppContext;
import com.sun.sgs.app.ClientSession;
import com.sun.sgs.app.DataManager;
import com.sun.sgs.app.ManagedReference;

import java.util.Hashtable;
import java.util.logging.Logger;

import common.datatypes.PlayerState;
import common.datatypes.IPlayerProperty;
import common.messages.IMessage;

/**
 * Esta clase proveer información a cerca del jugador como por ejemplo el
 * estado y sus propiedades. Además permite enviar mensajes por medio 
 * de la sessión.
 * 
 * @author Kopp Roberto &lt;robertokopp at hotmail dot com&gt;
 * @encoding UTF-8
 */
public final class Player extends DynamicEntity {
	
	/**  Para cumplir con la version de la clase Serializable. */
	private static final long serialVersionUID = 1L;
	
	/** Logger. */
	private static Logger logger = 
		Logger.getLogger(Player.class.getName());	
	
	/**
	 * Referencia a la sesion actual del player.
	 */
	private ManagedReference<ClientSession>	refSession = null;

	
    /**
	 * Conjunto de propiedades del jugador que no estan presentes en
	 * ModelAccess.
	 * Las propiedades que se guardan en este HahsTable, son las propiedades
	 * dinamicas, como puede ser la velocidad.
	 * 
	 * TODO El HashTable no es necesario, ya que IPlayerProperty contiene un
	 * identificador con su correspondiente valor. Por otro lado se podria 
	 * considerar la utilizacion de un HashTable por un tema de performance,
	 * pero tampoco es necesario, ya que no tiene una gran cantidad de 
	 * propidades. 
	 *   
	 * 
	 */
	private Hashtable<String, IPlayerProperty>	properties;
	
	/**
	 * El estado actual del jugador.
	 */
	private PlayerState	state;
	
	/**
	 * Este método es invocado para crear/recuperar un {@link Player} cuando un
	 * usuario se logea en el sistema.
	 * 
	 * Si el usuario entra al sistema por primera vez, se producirá una 
	 * excepcion debido a que que no se encuentra almacenado el {@link Player} 
	 * asociado a dicho jugador. Esta excepcion es capturada para crear al 
	 * nuevo Player y setear su id de entidad utilizando el método 
	 * {@link #getName()} de la clase {@link ClientSession}. Una vez 
	 * inicializado el Player, se lo almacena con {@link #setBinding()} para 
	 * posterior recuperación. 
	 * Por otro lado, si ya se ha registrado en el Object Store, entonces no
	 * se producirá la excepcion y se obtiene directamente el {@link Player} a
	 * travéz de la clase {@link DataManager} utilizando el método 
	 * {@link #getBinding()).
	 * 
	 * Además se chequea que un jugador no pueda logearse si el mismo ya se
	 * encuentra logeado en el sistema. Esto es para evitar que uno o mas 
     * jugadores no esten simultaneamente logeados en el sistema. 
	 * 
	 * @param session sesión correspondiente al juagdor.
	 * @return Player una instancia para el jugador.
	 */
	public static Player create(final ClientSession session) {
		// Data manager del sistema
		DataManager d = AppContext.getDataManager();
		// Jugador
		Player player = null;
		//el nombre de la sesion define el id del jugador
		String idPlayer = session.getName();
		
		try {	
			logger.info("Intentando recuperar una instancia del Object Store " 
					+ "para " + idPlayer);
			// recupero el Player a partir del nombre de la sesión utilizando
			// el dataManager.
			player = (Player) d.getBinding(idPlayer);
			// chequeo que uno o mas jugadores no esten simultaneamente 
			// logeados en el sistema.
			if (player.isConnected()) {
				return null;
			}
		} catch (Exception e) {
			logger.info("No existe ninguna instancia dentro del Object " 
					+ "Store para " + idPlayer);
			// creo un nuevo jugador 
			player = new Player();
			// seteo su id de entidad
			player.setIdEntity(idPlayer);
			// estado inicial del jugador
			PlayerState state = new PlayerState();
			state.setState(PlayerState.STATE_QUIET);
			player.setState(state);
			
			//Se setea las propiedades del jugador.
			Hashtable<String, IPlayerProperty> propiedades = 
				                     new Hashtable<String, IPlayerProperty>();
			player.setProperties(propiedades);

			//TODO falta setear el mundo por defecto y dentro del mismo la 
			//     posicion y el angulo. (Ver DinamycEntity).
			
			// registro el Player dentro del Object Store.
			d.setBinding(idPlayer , player);
		}
		
		return player;
	}
	
	/**
	 * Getter.
	 * 
	 * @param playerproperty identificador de la propiedad.
	 * @return property propiedad del jugador
	 */
	public IPlayerProperty getProperty(final String playerproperty) {
		return this.properties.get(playerproperty);
	}
	
	/**
	 * Retorna las propiedades del jugador que no estan presentes en
	 * ModelAccess.
	 * En la tabla de hash se guarda el par,
	 * identificador de la propiedad, con su correspondiente Valor.
	 * 
	 * @return properties propiedades del jugador
	 */
	
	public Hashtable<String, IPlayerProperty> getProperties() {
		return properties;
	}
	
	/**
	 * Se setea las propiedades del jugador que no estan presentes en
	 * ModelAccess.
	 * 
	 * @param pproperties propiedades del jugador
	 */
	public void setProperties(
			final Hashtable<String, IPlayerProperty> pproperties) {
		this.properties = pproperties;
	}
	
	/**
	 * Devuelve el estado del jugador.
	 * 
	 * @return state estado del jugador
	 */
	public PlayerState getState() {
		return state;
	}
	
	/**
	 * Se setea el estado del jugador.
	 * 
	 * @param pstate el estado del jugador
	 */
	public void setState(final PlayerState pstate) {
		this.state = pstate;
	}
	
	/**
	 * Getter.
	 * 
	 * @return session el objeto sesion asociado al player. 
	 */
	public ClientSession getSession() {
		if (refSession != null) {
			return refSession.get();
		}
		return null;
	}
	
	/**
	 * Se crea una referencia {@link ManagedReference} a la sesion del usuario
	 * {@link ClientSession} por medio del DataManager e indicando que el 
	 * Player se actualizara para establecer la referencia.
	 * 
	 * @param session sesion correspondiente al loggedIn del usuario. Si es el 
	 * valor de la sesion es null se producira una excepsion la cual es 
	 * capturada para establecer la referencia en null, lo que significa que no
	 * se hara uso de la sesion a la cual se hace referencia.
	 */
	public void setSession(final ClientSession session) {
        
    	DataManager dataMgr = AppContext.getDataManager();
        
    	dataMgr.markForUpdate(this);

        try {
        	this.refSession = dataMgr.createReference(session);	
        } catch (Exception e) {
			this.refSession = null;
		}
	}
	
	/**
	 * Send a IMessage directly to the player using client/server scheme.
	 * 
	 * @param message mensaje que se envia al usuario.
	 * 
	 * @see {@link ClientSession#send(java.nio.ByteBuffer)}
	 */
	public void send(final IMessage message) {
		getSession().send(message.toByteBuffer());
	}
	/**
	 * Por medio de la referencia a la secion, se obtiene el objeto 
	 * clientSession y con este se invoca al metodo isConnected para conocer el
	 * estado de coneccion (true/false) del jugador.
	 * 
	 * @return boolean si {@code true}, significa que el usuario esta conectado
	 *  al servidor.
	 */
	public boolean isConnected() {
		ClientSession session = getSession(); 
		if (session != null) {	
			return session.isConnected();
		}
		return false;
	}
}
