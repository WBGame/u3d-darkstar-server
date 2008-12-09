/**
 * 
 */
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
 *  
 */
public class Player extends DynamicEntity {
	
	/**
	 * Serialization code.
	 */
	private static final long serialVersionUID = -3708396766647144421L;

	/** 
	 * Logger.
	 */
	private static final Logger logger = 
		Logger.getLogger(Player.class.getName());
	
	/**
	 * Referencia a la secion actual del player.
	 */
	protected ManagedReference<ClientSession>		session;
	
	/**
	 * Conjunto de propiedades del jugador que no estan presentes en
	 * ModelAccess.
	 */
	protected Hashtable<String, IPlayerProperty>	properties;
	
	/**
	 * El estado actual deñ jugador.
	 */
	protected PlayerState							state;
	
	/**
	 * @param message
	 */
	public void send(final IMessage message) {
		// begin-user-code
		// TODO Apéndice de método generado automáticamente
		
		// end-user-code
	}
	
	/**
	 * @param property
	 * @return
	 */
	public IPlayerProperty getProperty(final String property) {
		// begin-user-code
		// TODO Apéndice de método generado automáticamente
		return null;
		// end-user-code
	}
	
	/**
	 * Getter.
	 * 
	 * @return ClientSession instance 
	 */
	public final ClientSession getSession() {
		return session.get();
	}
	
	/**
	 * Setter.
	 * 
	 * @param session Player ClientSession instance.
	 */
	public final void setSession(final ClientSession session) {
		DataManager dm = AppContext.getDataManager();
		try {
			this.session = dm.createReference( session );
		} catch ( Exception e ) {
			this.session = null;
			logger.log( Level.SEVERE , e.toString() );
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
	 * @param state the state to set
	 */
	public final void setState(final PlayerState state) {
		this.state = state;
	}
	
}
