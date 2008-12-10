package server.player;

import server.entity.DynamicEntity;

import com.sun.sgs.app.AppContext;
import com.sun.sgs.app.ClientSession;
import com.sun.sgs.app.DataManager;
import com.sun.sgs.app.ManagedReference;
import java.util.Hashtable;
import java.util.logging.Level;
import java.util.logging.Logger;

import common.datatypes.PlayerState;
import common.datatypes.IPlayerProperty;
import common.messages.IMessage;

/**
 * Esta clase proveer informacion a cerca del jugador como por ej el
 * estado y sus propiedades. Ademas permite enviar mensajes por medio 
 * de la session.
 * 
 * @author Kopp Roberto <robertokopp at hotmail dot com/>
 * @encoding UTF-8
 * 
 * TODO Implementar metodo send.
 * 
 */
public class Player extends DynamicEntity {
	
	/**  Para cumplir con la version de la clase Serializable. */
	
	private static final long serialVersionUID = 1L;

	/** El {@link Logger} para esta clase. */
	
    private final Logger logger = 
    	Logger.getLogger(Player.class.getName());

    /**
	 * Referencia a la sesion actual del player.
	 */
	private ManagedReference<ClientSession>	refSession;
	
	/**
	 * Conjunto de propiedades del jugador que no estan presentes en
	 * ModelAccess.
	 */
	private Hashtable<String, IPlayerProperty>	properties;
	
	/**
	 * El estado actual del jugador.
	 */
	private PlayerState	state;
	
	/**
	 * @Mock
	 * 
	 * @param message mensaje que se envia al usuario.
	 */
	public final void send(final IMessage message) {
		refSession.get().send(message.toByteBuffer());
	}
	
	/**
	 * 
	 * @param playerproperty identificador de la propiedad.
	 * @return property propiedad del jugador
	 */
	public final IPlayerProperty getProperty(final String playerproperty) {
		return this.properties.get(playerproperty);
	}
	
	/**
	 * @return session el objeto sesion asociado al player. 
	 */
	public final ClientSession getSession() {
		return refSession.get();
	}
	
	/**
	 * Se crea una referencia {@link ManagedReference} a la sesion del usuario
	 * {@link ClientSession} por medio del DataManager e indicando que el 
	 * Player se actualizara para establecer la referencia.
	 * 
	 * 
	 * @param session sesion correspondiente al loggedIn del usuario. Si es el 
	 * valor de la sesion es null se producira una excepsion la cual es 
	 * capturada para establecer la referencia en null, lo que significa que no
	 * se hara uso de la sesion a la cual se hace referencia.
	 */
	public final void setSession(final ClientSession session) {
        
    	DataManager dataMgr = AppContext.getDataManager();
        
    	dataMgr.markForUpdate(this);

        try {
        	this.refSession = dataMgr.createReference(session);	
            
        	logger.log(Level.INFO,
        			"Establecer una referencia a la sesión de {0} ",
        			session.getName());
        	
        } catch (Exception e) {
			this.refSession = null;
		}
	}
	
	/**
	 * Retorna las propiedades del jugador que no estan presentes en
	 * ModelAccess.
	 * 
	 * @return properties propiedades del jugador
	 */
	public final Hashtable<String, IPlayerProperty> getProperties() {
		return properties;
	}
	
	/**
	 * Se setea las propiedades del jugador que no estan presentes en
	 * ModelAccess.
	 * 
	 * @param pproperties propiedades del jugador
	 */
	public final void setProperties(
			final Hashtable<String, IPlayerProperty> pproperties) {
		this.properties = pproperties;
	}
	
	/**
	 * Devuelve el estado del jugador.
	 * 
	 * @return state estado del jugador
	 */
	public final PlayerState getState() {
		return state;
	}
	
	/**
	 * Se setea el estado del jugador.
	 * 
	 * @param pstate el estado del jugador
	 */
	public final void setState(final PlayerState pstate) {
		this.state = pstate;
	}
	
	/**
	 * Por medio de la referencia a la secion, se obtiene el objeto 
	 * clientSession y con este se invoca al metodo isConnected para conocer el
	 * estado de coneccion (true/false) del jugador.
	 * 
	 * @return boolean si {@code true}, significa que el usuario esta conectado
	 *  al servidor.
	 */
	public final boolean isConnected() {
		if (refSession != null) {	
			return refSession.get().isConnected();
		}
		return false;
	}
}
