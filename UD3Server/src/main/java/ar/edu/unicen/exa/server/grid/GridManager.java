/**
 * 
 */
package ar.edu.unicen.exa.server.grid;

import java.io.Serializable;
import com.sun.sgs.app.ManagedObject;
import java.util.Hashtable;
import com.sun.sgs.app.ManagedReference;

/** 
 *  Contiene referencias a todas las  {@link IGridStructure} de celdas. Se pueden agregar estructuras, remover estructuras como asi tambien obtenerla una estructura dado el identificador del mundo.
 * @generated "De UML a Java V5.0 (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public class GridManager implements Serializable, ManagedObject {
	/** 
	 *  Es una tabla de hash que contiene referencias  {@code ManagedReference} a las estructuras que mantiene.
	 * @generated "De UML a Java V5.0 (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	protected Hashtable worlds;

	/** 
	 *  Instancia unica singleton de la clase
	 * @generated "De UML a Java V5.0 (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private GridManager instance;

	/**
	 * Retorna la referencia a la estructura que se corresponde con el identificador pasado por parametro.
	 * @return 
	 * @param id 
	 * @generated "De UML a Java V5.0 (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public ManagedReference getStructure(Object id) {
		// begin-user-code
		// TODO Apéndice de método generado automáticamente
		return null;
		// end-user-code
	}

	/**
	 * Agrega a la tabla de hash la estructura pasada por parametro. Para esto debe encargarse de crear una   {@code ManagedReference} invocando al metodo   {@code createReference()} del   {@code DataManager} .
	 * @param structure 
	 * @generated "De UML a Java V5.0 (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void addStructure(IGridStructure structure) {
		// begin-user-code
		// TODO Apéndice de método generado automáticamente

		// end-user-code
	}

	/**
	 * Retorna la instancia singleton de la clase
	 * @return 
	 * @generated "De UML a Java V5.0 (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public static GridManager getInstance() {
		// begin-user-code
		// TODO Apéndice de método generado automáticamente
		return null;
		// end-user-code
	}

	/**
	 * Remueve de la tabla de hash la estructura asociada al identificador pasado por parametro.
	 * @param id 
	 * @generated "De UML a Java V5.0 (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void removeStructure(Object id) {
		// begin-user-code
		// TODO Apéndice de método generado automáticamente

		// end-user-code
	}
}