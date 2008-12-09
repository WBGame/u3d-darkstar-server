package ar.edu.unicen.exa.server.player;

import ar.edu.unicen.exa.server.entity.DynamicEntity;

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
 * @author Kopp Roberto <robertokopp at hotmail dot com>
 * @encoding UTF-8
 * 
 * TODO Javadoc
 */
public class Player extends DynamicEntity {
	
	/**  Para cumplir con la version de la clase Serializable. */
	private static final long serialVersionUID = 1L;

	/** El {@link Logger} para esta clase. */
    private static final Logger logger =
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
	 * @param message
	 */
	public final void send(final IMessage message) {
		refSession.get().send(message.toByteBuffer());
	}
	
	/**
	 * 
	 * @param property 
	 * @return property
	 */
	public final IPlayerProperty getProperty(final String property) {
		return this.properties.get(property);
	}
	
	/**
	 * @return session
	 */
	public final ClientSession getSession() {
		return refSession.get();
	}
	
	/**
	 * @param session the session to set
	 */
	public final void setSession(final ClientSession session) {
        
    	DataManager dataMgr = AppContext.getDataManager();
        
    	dataMgr.markForUpdate(this);

        try {
        	this.refSession = dataMgr.createReference(session);	
            
        	logger.log( 
            		Level.INFO, 
            		"Establecer una referencia a la sesión de {0} ",
            		session.getName()
            );
        	
        } catch (Exception e) {
			this.refSession = null;
		}
	}
	
	/**
	 * @return the properties
	 */
	public final Hashtable<String, IPlayerProperty> getProperties() {
		return properties;
	}
	
	/**
	 * @param properties the properties to set
	 */
	public final void setProperties(
			final Hashtable<String, IPlayerProperty> properties) {
		this.properties = properties;
	}
	
	/**
	 * @return the state
	 */
	public final PlayerState getState() {
		return state;
	}
	
	/**
	 * 
	 * @param state the state to set
	 */
	public final void setState(final PlayerState state) {
		this.state = state;
	}
	
	/**
	 * TODO javadoc 
	 */
	public final boolean isConnected(){
		if( refSession != null ) {	
			return refSession.get().isConnected();
		}
		return false;
	}
}