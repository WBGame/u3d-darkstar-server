/**
 * 
 */
package server.player;

import server.entity.DynamicEntity;

import com.sun.sgs.app.ClientSession;
import com.sun.sgs.app.ManagedReference;
import java.util.Hashtable;
import common.datatypes.PlayerState;
import common.datatypes.IPlayerProperty;
import common.messages.IMessage;

/**
 *  
 */
public class Player extends DynamicEntity {
	
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
	 * El estado actual de� jugador.
	 */
	protected PlayerState							state;
	
	/**
	 * @param message
	 */
	public void send(final IMessage message) {
		// begin-user-code
		// TODO Ap�ndice de m�todo generado autom�ticamente
		
		// end-user-code
	}
	
	/**
	 * @param property
	 * @return
	 */
	public IPlayerProperty getProperty(final String property) {
		// begin-user-code
		// TODO Ap�ndice de m�todo generado autom�ticamente
		return null;
		// end-user-code
	}
	
	/**
	 * @return the session
	 */
	public final ManagedReference<ClientSession> getSession() {
		return session;
	}
	
	/**
	 * @param session the session to set
	 */
	public final void setSession(final ManagedReference<ClientSession> session) {
		this.session = session;
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
