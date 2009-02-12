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
 * Esta clase proveer informacion a cerca del jugador como por ejemplo el 
 * estado y sus propiedades. Ademas permite enviar mensajes por medio de la
 * sesion.
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
	 * Referencia a la sesion actual del {@link Player}.
	 */
	private ManagedReference<ClientSession>	refSession = null;

	
    /**
	 * Conjunto de propiedades del jugador que no estan presentes en
	 * ModelAccess.
	 * Las propiedades que se guardan en este HahsTable, son las propiedades
	 * dinamicas, como puede ser la velocidad.
	 * 
	 * TODO El HashTable no es necesario, ya que {@link IPlayerProperty} 
	 * contiene un identificador con su correspondiente valor. Por otro lado se
	 * podria considerar la utilizacion de un HashTable por un tema de 
	 * performance, pero tampoco es necesario, ya que no tiene una gran 
	 * cantidad de propidades. 
	 * 
	 */
	private Hashtable<String, IPlayerProperty>	properties;
	
	/**
	 * El estado actual del {@link Player}.
	 */
	private PlayerState	state;
	
	/**
	 * Este metodo es invocado para crear/recuperar un {@link Player} cuando un
	 * usuario se logea en el sistema.
	 * 
	 * Si el usuario entra al sistema por primera vez, se producira una 
	 * excepcion debido a que que no se encuentra almacenado el {@link Player}. 
	 * Esta excepcion es capturada para crear al nuevo Player y setear su id de
	 * entidad utilizando el metodo {@link #getName()} de la clase 
	 * {@link ClientSession}. Una vez inicializado el Player, se lo almacena 
	 * con {@link #setBinding()} para su posterior recuperacion. 
	 * Por otro lado, si ya se ha registrado en el Object Store, entonces no
	 * se producira la excepcion y se obtiene directamente el {@link Player} a
	 * travez de la clase {@link DataManager} utilizando el metodo 
	 * {@link #getBinding()).
	 * 
	 * Ademas se chequea que un jugador no pueda logearse si el mismo ya se
	 * encuentra logeado en el sistema. Esto es para evitar que uno o mas 
     * jugadores no esten simultaneamente logeados en el sistema con el mismo
     * usuario. 
	 * 
	 * @param session sesion correspondiente al {@link Player}.
	 * @return Player una instancia para el {@link Player}.
	 */
	public static Player create(final ClientSession session) {
		// Data manager del sistema.
		DataManager d = AppContext.getDataManager();
		// Jugador.
		Player player = null;
		// El nombre de la sesion define el id del jugador.
		String idPlayer = session.getName();
		
		try {	
			logger.info("Intentando recuperar una instancia del Object Store " 
					+ "para " + idPlayer);
			// Recupero el Player a partir del nombre de la sesion utilizando
			// el dataManager.
			player = (Player) d.getBinding(idPlayer);
			// Chequeo que uno o mas jugadores no esten simultaneamente 
			// logeados en el sistema.
			if (player.isConnected()) {
				return null;
			}
		} catch (Exception e) {
			logger.info("No existe ninguna instancia dentro del Object " 
					+ "Store para " + idPlayer);
			// Creo un nuevo jugador 
			player = new Player();
			// Seteo su id de entidad
			player.setIdEntity(idPlayer);
			// Estado inicial del jugador
			PlayerState state = new PlayerState();
			state.setState(PlayerState.STATE_QUIET);
			player.setState(state);
			
			//Se setea las propiedades del jugador.
			Hashtable<String, IPlayerProperty> propiedades = 
				                     new Hashtable<String, IPlayerProperty>();
			player.setProperties(propiedades);

			//TODO falta setear el mundo por defecto y dentro del mismo la 
			//     posicion y el angulo. (Ver DinamycEntity).
			
			// Registro el Player dentro del Object Store.
			d.setBinding(idPlayer , player);
		}
		
		return player;
	}
	
	/**
	 * Dado el identificador de una propiedad, retorna dicha propiedad.
	 * 
	 * @param playerproperty identificador de la propiedad.
	 * @return property propiedad del {@link Player}.
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
	 * @return properties propiedades del {@link Player}.
	 */
	
	public Hashtable<String, IPlayerProperty> getProperties() {
		return properties;
	}
	
	/**
	 * Se setea las propiedades del {@link Player} que no estan presentes en
	 * {@link ModelAccess}.
	 * 
	 * @param pproperties propiedades del {@link Player}.
	 */
	public void setProperties(
			final Hashtable<String, IPlayerProperty> pproperties) {
		this.properties = pproperties;
	}
	
	/**
	 * Devuelve el {@link PlayerState}(estado) del {@link Player}.
	 * 
	 * @return state estado del {@link Player}.
	 */
	public PlayerState getState() {
		return state;
	}
	
	/**
	 * Se setea el {@link PlayerState}(estado) del {@link Player}.
	 * 
	 * @param pstate el {@link PlayerState}(estado) {@link Player}.
	 */
	public void setState(final PlayerState pstate) {
		this.state = pstate;
	}
	
	/**
	 * Retorna el objeto {@link ClientSession} asociado al {@link Player}.
	 * 
	 * @return session el objeto sesion asociado al {@link Player}. 
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
	 * {@link Player} se actualizara para establecer la referencia.
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
	 * Enviar un {@link IMessage} directamente al {@link Player} a traves del 
	 * cliente/servidor.
	 * 
	 * @param message mensaje que se envia al usuario.
	 * 
	 * @see {@link ClientSession#send(java.nio.ByteBuffer)}
	 */
	public void send(final IMessage message) {
		getSession().send(message.toByteBuffer());
	}
	/**
	 * Este metodo indica el estado de la conexion de un {@link Player}},
	 * devolviendo True/False.
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
