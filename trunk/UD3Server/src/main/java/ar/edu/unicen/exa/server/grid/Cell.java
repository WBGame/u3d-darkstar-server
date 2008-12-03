package ar.edu.unicen.exa.server.grid;

import java.awt.Rectangle;
import java.io.Serializable;

import com.jme.math.Vector3f;
import com.sun.sgs.app.AppContext;
import com.sun.sgs.app.Channel;
import com.sun.sgs.app.ClientSession;
import com.sun.sgs.app.ManagedReference;
import common.messages.IMessage;

/**
 * Representa una zona fisica del mundo. Esta zona esta delimitada por los
 * bounds. Ademas, esta zona esta asociada a un unico {@code Channel} , es decir
 * hay una correspondencia uno a uno entre celdas y canales.
 * 
 * @generated "De UML a Java V5.0 
 * 		(com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public class Cell implements Serializable {
	/** The version of the serialized form of this class. */
	private static final long serialVersionUID = 1301727798124952702L;

	/**
	 * Es una referencia {@code ManagedReference} a la {@link IGridStructure}
	 * contenedora de la celda.
	 * 
	 * @generated "De UML a Java V5.0 
	 * 		(com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private ManagedReference<IGridStructure> structure;

	/**
	 * Es la identificacion unica de una celda. No se debe repetir para niguna
	 * celda de una misma estructura.
	 * 
	 * @generated "De UML a Java V5.0 
	 * 		(com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private Object id;

	/**
	 * Es una referencia {@code ManagedReference} al {@code Channel} asociado a
	 * la celda.
	 * 
	 * @generated "De UML a Java V5.0 
	 * 		(com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private ManagedReference<Channel> channel;

	/**
	 * Determina el espacio circundado por la celda en el espacio fisico del
	 * mundo.
	 * 
	 * @generated "De UML a Java V5.0 
	 * 		(com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private Rectangle bounds;

	/**
	 * Creador.
	 * @param cellId identificador de la celda.
	 * @param cellBunds límites de la celda.
	 * @param parent Estructura a la que pertenece la celda.
	 * @generated "De UML a Java V5.0 
	 * 		(com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public Cell(final Object cellId, final Rectangle cellBunds, 
			final IGridStructure parent) {
		this.id = cellId;
		this.bounds = cellBunds;
		structure = AppContext.getDataManager().createReference(parent);
	}

	/**
	 * Retorna el identificador de la celda.
	 * 
	 * @return el identificador de esta instancia de celda.
	 * @generated "De UML a Java V5.0 
	 * 		(com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public final Object getId() {
		return id;
	}

	/**
	 * Retorna la referencia {@code ManagedReference} del canal asociado a la
	 * celda.
	 * 
	 * @return el Channel de esta celda.
	 * @generated "De UML a Java V5.0 
	 * 		(com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public final Channel getChannel() {
		return channel.get();
	}

	/**
	 * Asocia un canal a la celda. Dado que el canal es un objeto {@code
	 * ManagedObject} se debe crear la referencia {@code ManagedReference} a ese
	 * canal invocando al metodo {@code createReference()} del {@code
	 * DataManager} .
	 * 
	 * @param cellChannel canal que se debe asociar a la celda.
	 * @generated "De UML a Java V5.0 
	 * 		(com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public final void setChannel(final Channel cellChannel) {
		this.channel = AppContext.getDataManager().createReference(cellChannel);
	}

	/**
	 * Retorna el espacio circundado por la celda.
	 * 
	 * @return Los límites de esta celda.
	 * @generated "De UML a Java V5.0 
	 * 		(com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public final Rectangle getBounds() {
		return bounds;
	}

	/**
	 * Establece el espacio circundado por la celda.
	 * 
	 * @param cellBounds Límites de esta celda.
	 * @generated "De UML a Java V5.0 
	 * 		(com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
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
	 * @generated "De UML a Java V5.0 
	 * 		(com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public final IGridStructure getStructure() {
		return structure.get();
	}

	/**
	 * Subscribe al jugador pasado por parametro {@code ClientSession} al canal
	 * contenido por la celda.
	 * 
	 * @param client jugador a subscribir.
	 * @generated "De UML a Java V5.0 
	 * 		(com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public final void joinToChannel(final ClientSession client) {
		getChannel().join(client);
	}

	/**
	 * Desubscribe al jugador pasado por parametro {@code ClientSession} del
	 * canal contenido por la celda.
	 * 
	 * @param client jugador a desuscribir.
	 * @generated "De UML a Java V5.0 
	 * 		(com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public final void leaveFromChannel(final ClientSession client) {
		getChannel().leave(client);
	}

	/**
	 * Determina si la posicion dada esta dentro de la celda. Para ello utiliza
	 * la variable de instancia {@link bounds}
	 * 
	 * @return true si la posición dada está dentro de esta celda. false en otro
	 * caso.
	 * @param position posición a evaluar.
	 * @generated "De UML a Java V5.0 
	 * 		(com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
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
	 * @generated "De UML a Java V5.0 
	 * 		(com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public final void send(final IMessage msg, final ClientSession player) {
		getChannel().send(player, msg.toByteBuffer());
	}
}
