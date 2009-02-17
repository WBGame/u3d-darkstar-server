package ar.edu.unicen.exa.server.grid;

import java.awt.Rectangle;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.util.logging.Logger;

import ar.edu.unicen.exa.server.communication.processors.ServerMsgProcessor;
import ar.edu.unicen.exa.server.communication.processors.ServerMsgProssesorFactory;
import ar.edu.unicen.exa.server.grid.id.IBindingID;
import ar.edu.unicen.exa.server.grid.id.IDManager;
import ar.edu.unicen.exa.server.player.Player;

import com.jme.math.Vector3f;
import com.sun.sgs.app.AppContext;
import com.sun.sgs.app.Channel;
import com.sun.sgs.app.ChannelListener;
import com.sun.sgs.app.ChannelManager;
import com.sun.sgs.app.ClientSession;
import com.sun.sgs.app.DataManager;
import com.sun.sgs.app.Delivery;
import com.sun.sgs.app.ManagedReference;

import common.exceptions.MalformedMessageException;
import common.exceptions.UnsopportedMessageException;
import common.messages.IMessage;
import common.messages.MessageFactory;
import common.util.ChannelNameParser;

/**
 * Representa una zona fisica del mundo. Esta zona esta delimitada por los
 * bounds. Ademas, esta zona esta asociada a un unico {@code Channel} , es decir
 * hay una correspondencia uno a uno entre celdas y canales.
 * 
 * @author Sebastián Perruolo &lt;sebastianperruolo at gmail dot com &gt;
 * @encoding UTF-8
 */
public class Cell implements 
		Serializable, IBindingID, ChannelListener {
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

		IDManager.setNewID(this);

		this.bounds = cellBunds;
		logger.info("x= " + bounds.getX() + " y= " + bounds.getY());
		if (parent != null) {
			refStructure = AppContext.getDataManager().createReference(parent);
		}
    	
		ChannelManager channelMgr = AppContext.getChannelManager();
        
		//TODO implementar un channel manager para poder tener varios tipos de 
		//     canales según el mecanismo de tipos de canales implementado en
		//     el common.
		String channelName = ChannelNameParser.MOVE_CHANNEL_IDENTIFIER 
							 + '_' + id;
        Channel channel = channelMgr.createChannel(channelName, 
        									  this, 
                                              Delivery.RELIABLE);
        this.setChannel(channel);
	}

	/**
	 * Retorna el identificador de la celda.
	 * 
	 * @return el identificador de esta instancia de celda.
	 */
	@Override
	public final long getId() {
		return Long.parseLong(id);
	}
	
	/**
	 * Setea el identificador de la celda.
	 * @param anId el identificador de esta instancia de celda.
	 */
	@Override
	public final void setId(final long anId) {
		this.id = Long.toString(anId);
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

	/**
	 * Indica si este objeto es igual que el objeto recibido por
	 * parámetro.
	 * 
	 * @param obj Objeto a comparar con este objeto.
	 * @return true si el objeto obj es igual que este objeto, 
	 * 		false en otro caso.
	 */
	@Override
	public final boolean equals(final Object obj) {
		if (obj == null) {
			return false;
		}
		if (obj instanceof Cell) {
			Cell other = (Cell) obj;
			return this.getId() == other.getId();
		}
		return false;
	}
	
	/**
	 * Retorna un valor <i>hash code</i> para este objeto.
	 * @return un valor <i>hash code</i> para este objeto.
	 * @see Object#hashCode()
	 */
	@Override
	public final int hashCode() {
		// para satisfacer al checkstyle cuando redefiní el equals.
		return super.hashCode();
	}

	
	/**
	 * Procesa los mensajes que llegan por los canales. Para esto debe generar
	 * un {@code IMessage} dado los datos recibidos y generar un 
	 * {@code IProcessor} que procese el mensaje. 
	 * 
	 * @param channel a channel
	 * @param session the sending client session
	 * @param msg a message
	 * 
	 * @encoding UTF-8
	 */
	@Override
	public final void receivedMessage(final Channel channel,
			final ClientSession session, final ByteBuffer msg) {
		try {
			// Regenerar el objeto mensaje
			IMessage iMessage = MessageFactory.getInstance().createMessage(msg);

			// Crear el procesador asociado al mismo.
			ServerMsgProcessor processor = 
				(ServerMsgProcessor) ServerMsgProssesorFactory.getInstance()
				.createProcessor(iMessage.getType());

			logger.info("Llego mensaje en Channel " + channel.getName() 
					+ ", tipo " + iMessage.getType());
			
			DataManager dataMgr = AppContext.getDataManager();

			//recuperar el jugador desde el DataManager
			Player player = (Player) dataMgr.getBinding(
					session.getName() 
			);

			// Inicialización para ejecutar el proceso asociado al mensaje.
			processor.setPlayerAssociated(player);
			processor.setCellAssociated(this);

			// Ejecución del procesamiento.
			processor.process(iMessage);
		} catch (MalformedMessageException e) {
			logger.warning("Mensaje inconsistente.");		
			e.printStackTrace();
		} catch (UnsopportedMessageException e) {
			logger.warning("Mensaje inexistente.");
			e.printStackTrace();
		}		
	}
	
}
