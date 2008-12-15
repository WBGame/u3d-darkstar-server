package ar.edu.unicen.exa.server.player;

import ar.edu.unicen.exa.server.communication.tasks.TaskCommFactory;
import ar.edu.unicen.exa.server.communication.tasks.TaskCommunication;
import common.exceptions.MalformedMessageException;
import common.exceptions.UnsopportedMessageException;
import common.messages.IMessage;
import common.messages.MessageFactory;

import com.sun.sgs.app.AppContext;
import com.sun.sgs.app.ClientSession;
import com.sun.sgs.app.ClientSessionListener;
import com.sun.sgs.app.DataManager;

import java.io.Serializable;
import com.sun.sgs.app.ManagedReference;
import java.nio.ByteBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;

/** 
 * Implementacion de {@code ClientSessionListener}. 
 * Esta asociado uno a uno con cada jugador ( {@link Player} ) del sistema. 
 * Debe atender peticiones de desconexion y mensajes entrantes.
 * 
 * @author Pablo Inchausti <inchausti.pablo at gmail dot com/> 
 * @encoding UTF-8 
 */
public class UserSessionListener 
implements ClientSessionListener, Serializable {

	/**
	 * Serialization code. 
	 */
	private static final long serialVersionUID = 4348700548351927735L;

	/** 
	 * Logger para esta clase.
	 */
	private final static Logger logger = 
		Logger.getLogger(UserSessionListener.class.getName());

	/**
	 * Es una referencia {@code ManagedReference} al {@link Player}.
	 *
	 */
	private ManagedReference<Player> playerRef;

	/**
	 * Retorna un ( {@link Player} ) a partir de la referencia 
	 * {@code ManagedReference} al jugador asociado.
	 * 
	 * @return Player jugador que hace referencia el {@code ManagedReference}.
	 */
	public final Player getPlayer() {
		return playerRef.get();
	}

	/**
	 * Establece el {@link Player} para capturar los eventos de este jugador.
	 * Se crea la referencia {@code ManagedReference} al jugador invocando al
	 * método {@code createReference()} del {@code DataManager}.
	 * 
	 * @param player instancia de un jugador.
	 */
	public final void setPlayer(final Player player) {
		if (player == null) {
            throw new NullPointerException(
            		"No existe una instancia para la clase Player");
		}
    	DataManager dataMgr = AppContext.getDataManager();
        
        try {
        	this.playerRef = dataMgr.createReference(player);
            
        	logger.log(
            		Level.INFO, "Establecer una referencia al Player: {0} ",
            		player.getIdEntity()
            );
        	
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}

	/**
	 *  
	 * Create a Imessage with the data received and create a new task with
	 * the data
	 * TODO add verifications for correct type of data received. 
	 * Need to implement exceptions
	 * @param msg mensaje que recibe de un usuario.
	 */
	public final void receivedMessage(final ByteBuffer msg) {
		//create a new task for the message
		IMessage iMessage = null;
		try {
			iMessage = MessageFactory.getInstance().createMessage(msg);
		} catch (MalformedMessageException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsopportedMessageException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		TaskCommunication taskCommunication = TaskCommFactory.getInstance()
					.createComTask(iMessage);
		AppContext.getTaskManager().scheduleTask(taskCommunication);
	}

	/**
	 * Desconecta al jugador de la sesión que tiene establecida con el 
	 * servidor. 
	 * 
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
		
		// la sesión es inutilizada para un usuario desconectado. 
		getPlayer().setSession(null);
	}
}
