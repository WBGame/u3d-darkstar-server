package ar.edu.unicen.exa.server.grid;

import java.awt.Rectangle;
import java.io.Serializable;
import java.util.logging.Logger;

//import ar.edu.unicen.exa.server.player.Player;

import com.jme.math.Vector3f;
import com.sun.sgs.app.AppContext;
import com.sun.sgs.app.Channel;
import com.sun.sgs.app.ChannelManager;
import com.sun.sgs.app.ClientSession;
import com.sun.sgs.app.Delivery;
import com.sun.sgs.app.ManagedReference;
import common.messages.IMessage;



/**
 * Representa una zona fisica del mundo. Esta zona esta delimitada por los
 * bounds. Ademas, esta zona esta asociada a un unico {@code Channel} , es decir
 * hay una correspondencia uno a uno entre celdas y canales.
 */


public class Cell implements Serializable {
	/** The version of the serialized form of this class. */
	private static final long serialVersionUID = 1301727798124952702L;

	
	/** Logger. */
	private static Logger logger = 
		Logger.getLogger(Cell.class.getName());
	/**
	 * Es una referencia {@code ManagedReference} a la {@link IGridStructure}
	 * contenedora de la celda.
	 * 
	 */
	private ManagedReference<IGridStructure> refStructure;

	/**
	 * Es la identificacion unica de una celda. No se debe repetir para niguna
	 * celda de una misma estructura.
	 * 
	 */
	private String id;

	/**
	 * Es una referencia {@code ManagedReference} al {@code Channel} asociado a
	 * la celda.
	 */
	private ManagedReference<Channel> refChannel;

	/**
	 * Determina el espacio circundado por la celda en el espacio fisico del
	 * mundo.
	 */
	private Rectangle bounds;

	/**
	 * Creador.
	 * 
	 * @param cellBunds límites de la celda.
	 * @param parent Estructura a la que pertenece la celda.
	 */
	public Cell(final Rectangle cellBunds, 
			final IGridStructure parent) {
		//XXX parte de la inicializacion del sistema. 
		//generador de nuevos ids unicos para cada celda
		this.id = CellIDGenerator.getNextID();
		logger.info("idcell = " + id);
		this.bounds = cellBunds;
		logger.info("x= " + bounds.getX() + " y= " + bounds.getY());
		if (parent != null) {
			refStructure = AppContext.getDataManager().createReference(parent);
		}
    	
		ChannelManager channelMgr = AppContext.getChannelManager();
        
        //se crea el canal con nombre de channel + el id de la celda 
		//corresponiente, además se indica qué la clase ChannelMessageListener
		//será la encargada de recibir los mensajes que se envian a dicho canal
    	
    	String channelName = "channel_" + id; 
    	
        Channel channel = channelMgr.createChannel(channelName, 
        									  new ChannelMessageListener(), 
                                              Delivery.RELIABLE);
        this.setChannel(channel);
	}

	/**
	 * Retorna el identificador de la celda.
	 * 
	 * @return el identificador de esta instancia de celda.
	 */
	public final String getId() {
		return id;
	}

	/**
	 * Retorna la referencia {@code ManagedReference} del canal asociado a la
	 * celda.
	 * 
	 * @return el Channel de esta celda.
	 */
	public final Channel getChannel() {
		return refChannel.get();
	}

	/**
	 * Asocia un canal a la celda. Dado que el canal es un objeto {@code
	 * ManagedObject} se debe crear la referencia {@code ManagedReference} a ese
	 * canal invocando al metodo {@code createReference()} del {@code
	 * DataManager} .
	 * 
	 * @param cellChannel canal que se debe asociar a la celda.
	 */
	public final void setChannel(final Channel cellChannel) {
		this.refChannel = AppContext.getDataManager()
				.createReference(cellChannel);
	}

	/**
	 * Retorna el espacio circundado por la celda.
	 * 
	 * @return Los límites de esta celda.
	 */
	public final Rectangle getBounds() {
		return bounds;
	}

	/**
	 * Establece el espacio circundado por la celda.
	 * 
	 * @param cellBounds Límites de esta celda.
	 */
	public final void setBounds(final Rectangle cellBounds) {
		this.bounds = cellBounds;
	}

	/**
	 * Retorna la referencia a la {@link IGridStructure} contenedora de la
	 * celda. Dado que la estructura es un objeto {@code ManagedObject} dicha
	 * referencia debe ser de tipo {@code ManagedReference} .
	 * 
	 * @return referencia a la estructura contenedora.
	 */
	public final IGridStructure getStructure() {
		return refStructure.get();
	}

	/**
	 * Subscribe al jugador pasado por parametro {@code ClientSession} al canal
	 * contenido por la celda.
	 * 
	 * @param client jugador a subscribir.
	 */
	public final void joinToChannel(final ClientSession client) {
		getChannel().join(client);
	}

	/**
	 * Desubscribe al jugador pasado por parametro {@code ClientSession} del
	 * canal contenido por la celda.
	 * 
	 * @param client jugador a desuscribir.
	 */
	public final void leaveFromChannel(final ClientSession client) {
		getChannel().leave(client);
	}

	/**
	 * Determina si la posicion dada esta dentro de la celda. Para ello utiliza
	 * la variable de instancia {@link bounds}
	 * 
	 * @return true si la posición dada está dentro de esta celda. 
	 * false en otro caso.
	 * 
	 * @param position posición a evaluar.
	 */
	public final boolean isInside(final Vector3f position) {
		//TODO verificar este metodo ya que Vector3f tiene 3 coordenadas
		return bounds.contains(position.getX(), position.getY());
	}

	/**
	 * Envia el mensaje {@code IMessage} del jugador dado a todos los jugadores
	 * asociados al canal contenido por la celda.
	 * 
	 * @param msg mensaje a enviar.
	 * @param player jugador que disparó el mensaje
	 */
	public final void send(final IMessage msg, final ClientSession player) {
		getChannel().send(player, msg.toByteBuffer());
	}
}
