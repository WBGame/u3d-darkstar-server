/**
 * 
 */
package server.player;

import com.sun.sgs.app.ClientSessionListener;
import java.io.Serializable;
import server.communication.processors.ServerMsgProcessor;
import server.communication.tasks.TaskCommFactory;
import com.sun.sgs.app.ManagedReference;
import java.nio.ByteBuffer;

/** 
 *  Implementacion de  {@code ClientSessionListener} . Esta asociado uno a uno con cada jugador ( {@link Player} ) del sistema. Debe atender peticiones de desconexion y mensajes entrantes.
 * @generated "De UML a Java V5.0 (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public class UserSessionListener implements ClientSessionListener, Serializable {
	/** 
	 * @generated "De UML a Java V5.0 (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	protected ManagedReference player;

	/**
	 * Retorna la referencia   {@code ManagedReference} del jugador asociado al listener.
	 * @return 
	 * @generated "De UML a Java V5.0 (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public ManagedReference getPlayer() {
		// begin-user-code
		// TODO Apéndice de método generado automáticamente
		return null;
		// end-user-code
	}

	/**
	 * Establece el jugador   {@link Player} . Debe crear la referencia   {@code ManagedReference} invocando al metodo   {@code createReference()} del   {@code DataManager} .
	 * @param player 
	 * @generated "De UML a Java V5.0 (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void setPlayer(Player player) {
		// begin-user-code
		// TODO Apéndice de método generado automáticamente

		// end-user-code
	}

	/**
	 * Se invoca a este metodo callback cuando quiere el cliente manda un mensaje directo al servidor.
	 * @param msg 
	 * @generated "De UML a Java V5.0 (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void receivedMessage(ByteBuffer msg) {
		// begin-user-code
		// TODO Apéndice de método generado automáticamente

		// end-user-code
	}

	/**
	 * Desconecta al jugador de la sesion que tiene establecida con el servidor.
	 * @param arg0 
	 * @generated "De UML a Java V5.0 (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void disconnected(boolean arg0) {
		// begin-user-code
		// TODO Apéndice de método generado automáticamente

		// end-user-code
	}
}