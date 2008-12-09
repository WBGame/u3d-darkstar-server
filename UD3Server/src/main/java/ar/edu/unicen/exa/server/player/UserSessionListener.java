package ar.edu.unicen.exa.server.player;

import com.sun.sgs.app.AppContext;
import com.sun.sgs.app.ClientSession;
import com.sun.sgs.app.ClientSessionListener;
import com.sun.sgs.app.DataManager;

import java.io.Serializable;
import ar.edu.unicen.exa.server.communication.processors.ServerMsgProcessor;
import ar.edu.unicen.exa.server.communication.tasks.TaskCommFactory;
import com.sun.sgs.app.ManagedReference;
import java.nio.ByteBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;

/** 
 * Implementacion de {@code ClientSessionListener}. 
 * Esta asociado uno a uno con cada jugador ( {@link Player} ) del sistema. 
 * Debe atender peticiones de desconexion y mensajes entrantes.
 * 
 * @encoding UTF-8 
 */
public class UserSessionListener 
implements ClientSessionListener, Serializable {

	/**
	 * Serialization code. 
	 */
	private static final long serialVersionUID = 4348700548351927735L;

	/** 
	 * Logger.
	 */
	private static final Logger logger = 
		Logger.getLogger(UserSessionListener.class.getName());

	/** 
	 * Reference to player object.
	 */
	protected ManagedReference<Player> playerRef;

	/**
	 * Retorna el objeto ( {@link Player} ) a partir de la referencia 
	 * {@code ManagedReference} al jugador asociado.
	 * @return Player jugador que refiere el {@code ManagedReference}.
	 */
	public final Player getPlayer() {
		return playerRef.get();
	}

	/**
	 * Establece el {@link Player}. Se crea la referencia 
	 * {@code ManagedReference} al jugador invocando al método 
	 * {@code createReference()} del {@code DataManager} .
	 * @param player instancia de un jugador.
	 */
	public final void setPlayer(final Player player) {
		if (player == null)
            throw new NullPointerException("null player");
        
    	DataManager dataMgr = AppContext.getDataManager();
        
        try {
        	this.playerRef = dataMgr.createReference(player);	
		} catch (Exception e) {
			this.playerRef = null;
		}
		
        logger.log(
        		Level.INFO, "Establecer una referencia al Player: {0} ",
        		player.getIdEntity()
        );
	}

	/**
	 * Se invoca a este metodo automaticamente cuando el cliente envia un 
	 * mensaje directamente al servidor.
	 * @param msg mensaje que recibe de un usuario.
	 */
	public final void receivedMessage(final ByteBuffer msg) {
		ClientSession session = getPlayer().getSession();
		logger.info( 
				"AppServer: Reciviendo el mensaje del usuario "
				+ session.getName()		
		);
	}

	/**
	 * Desconecta al jugador de la sesion que tiene establecida con el 
	 * servidor.
	 * @param graceful si {@code true}, el cliente se desconecta
	 *        correctamente.
	 */
	public final void disconnected(final boolean graceful) {
		ClientSession session = getPlayer().getSession();
		String grace = graceful ? "correctamente" : "forzadamente";
		logger.log(
				Level.INFO,
				"El usuario {0} se ha desconectado {1}",
				new Object[] { session.getName(), grace } 
		);
	}
}