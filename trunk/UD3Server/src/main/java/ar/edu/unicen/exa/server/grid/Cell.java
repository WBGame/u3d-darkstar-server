/**
 * 
 */
package server.grid;

import java.io.Serializable;
import com.sun.sgs.app.ManagedReference;
import java.awt.Rectangle;
import com.sun.sgs.app.Channel;
import com.sun.sgs.app.ClientSession;
import com.jme.math.Vector3f;
import common.messages.IMessage;

/** 
 *  Representa una zona fisica del mundo. Esta zona esta delimitada por los bounds. Ademas, esta zona esta asociada a un unico  {@code Channel} , es decir hay una correspondencia uno a uno entre celdas y canales.
 * @generated "De UML a Java V5.0 (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public class Cell implements Serializable {
	/** 
	 *  Es una referencia  {@code ManagedReference} a la  {@link IGridStructure} contenedora de la celda.
	 * @generated "De UML a Java V5.0 (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	protected ManagedReference structure;

	/** 
	 *  Es la identificacion unica de una celda. No se debe repetir para niguna celda de una misma estructura.
	 * @generated "De UML a Java V5.0 (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	protected Object id;

	/** 
	 *  Es una referencia  {@code ManagedReference} al  {@code Channel} asociado a la celda.
	 * @generated "De UML a Java V5.0 (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	protected ManagedReference channel;

	/** 
	 *  Determina el espacio circundado por la celda en el espacio fisico del mundo.
	 * @generated "De UML a Java V5.0 (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	protected Rectangle bounds;

	/**
	 * @param id
	 * @param bounds
	 * @param parent
	 * @generated "De UML a Java V5.0 (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public Cell(Object id, Object bounds, ManagedReference parent) {
		// begin-user-code
		// TODO Apéndice de constructor generado automáticamente
		// end-user-code
	}

	/**
	 * Retorna el identificador de la celda.
	 * @return 
	 * @generated "De UML a Java V5.0 (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public Object getId() {
		// begin-user-code
		// TODO Apéndice de método generado automáticamente
		return null;
		// end-user-code
	}

	/**
	 * Retorna la referencia   {@code ManagedReference} del canal asociado a la celda.
	 * @return 
	 * @generated "De UML a Java V5.0 (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public ManagedReference getChannel() {
		// begin-user-code
		// TODO Apéndice de método generado automáticamente
		return null;
		// end-user-code
	}

	/**
	 * Asocia un canal a la celda. Dado que el canal es un objeto   {@code ManagedObject} se debe crear la referencia   {@code ManagedReference} a ese canal invocando al metodo   {@code createReference()} del   {@code DataManager} .
	 * @param channel 
	 * @generated "De UML a Java V5.0 (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void setChannel(Channel channel) {
		// begin-user-code
		// TODO Apéndice de método generado automáticamente

		// end-user-code
	}

	/**
	 * Retorna el espacio circundado por la celda.
	 * @return 
	 * @generated "De UML a Java V5.0 (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public Rectangle getBounds() {
		// begin-user-code
		// TODO Apéndice de método generado automáticamente
		return null;
		// end-user-code
	}

	/**
	 * Establece el espacio circundado por la celda.
	 * @param bounds 
	 * @generated "De UML a Java V5.0 (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void setBounds(Rectangle bounds) {
		// begin-user-code
		// TODO Apéndice de método generado automáticamente

		// end-user-code
	}

	/**
	 * Retorna la referencia a la   {@link IGridStructure} contenedora de la celda.  Dado que la estructura es un objeto   {@code ManagedObject} dicha referencia debe ser de tipo   {@code ManagedReference} .
	 * @return 
	 * @generated "De UML a Java V5.0 (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public ManagedReference getStructure() {
		// begin-user-code
		// TODO Apéndice de método generado automáticamente
		return null;
		// end-user-code
	}

	/**
	 * Subscribe al jugador pasado por parametro   {@code ClientSession} al canal contenido por la celda.
	 * @param client 
	 * @generated "De UML a Java V5.0 (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void joinToChannel(ClientSession client) {
		// begin-user-code
		// TODO Apéndice de método generado automáticamente

		// end-user-code
	}

	/**
	 * Desubscribe al jugador pasado por parametro   {@code ClientSession} del canal contenido por la celda.
	 * @param client 
	 * @generated "De UML a Java V5.0 (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void leaveFromChannel(ClientSession client) {
		// begin-user-code
		// TODO Apéndice de método generado automáticamente

		// end-user-code
	}

	/**
	 * Determina si la posicion dada esta dentro de la celda. Para ello utiliza la variable de instancia   {@link bounds}
	 * @return 
	 * @param position 
	 * @generated "De UML a Java V5.0 (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public Object isInside(Vector3f position) {
		// begin-user-code
		// TODO Apéndice de método generado automáticamente
		return null;
		// end-user-code
	}

	/**
	 * Envia el mensaje   {@code IMessage} del jugador dado a todos los jugadores asociados al canal contenido por la celda.
	 * @param msg 
	 * @param player 
	 * @generated "De UML a Java V5.0 (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void send(IMessage msg, ClientSession player) {
		// begin-user-code
		// TODO Apéndice de método generado automáticamente

		// end-user-code
	}
}