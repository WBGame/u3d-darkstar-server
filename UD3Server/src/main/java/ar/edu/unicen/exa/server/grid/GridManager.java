package ar.edu.unicen.exa.server.grid;

import java.io.Serializable;
import java.util.Hashtable;

import com.sun.sgs.app.AppContext;
import com.sun.sgs.app.ManagedObject;
import com.sun.sgs.app.ManagedReference;

/** 
 *  Contiene referencias a todas las  {@link IGridStructure} de celdas. Se 
 *  pueden agregar estructuras, remover estructuras como asi tambien obtenerla
 *  una estructura dado el identificador del mundo.
 *  
 * @generated "De UML a Java V5.0 
 * 			(com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public final class GridManager implements Serializable, ManagedObject {
	/** The version of the serialized form of this class. */
	private static final long serialVersionUID = -7374467626346407012L;

	/** 
	 *  Es una tabla de hash que contiene referencias  {@code ManagedReference}
	 *  a las estructuras que mantiene.
	 *  
	 * @generated "De UML a Java V5.0 
	 * 		(com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private Hashtable<String, ManagedReference<IGridStructure>> worlds;

	/** 
	 *  Instancia unica singleton de la clase.
	 *  
	 * @generated "De UML a Java V5.0 
	 * 		(com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private static GridManager instance;

	/**
	 * Metodo privado para implementar un singleton.
	 */
	private GridManager() {
		
	}
	/**
	 * Retorna la estructura que se corresponde con el
	 * identificador pasado por parametro.
	 * 
	 * @return estructura que se corresponde con el identificador
	 * @param id identificador de la estructura a obtener
	 * @generated "De UML a Java V5.0 
	 * 		(com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public IGridStructure getStructure(final Object id) {
		ManagedReference<IGridStructure> result = worlds.get(id.toString());
		if (result == null) {
			return null;
		}
		return result.get();
	}

	/**
	 * Agrega a la tabla de hash la estructura pasada por parametro. Para esto
	 * debe encargarse de crear una {@code ManagedReference} invocando al
	 * metodo {@code createReference()} del {@code DataManager}.
	 * 
	 * @param structure estructura a almacenar
	 * @generated "De UML a Java V5.0 
	 * 		(com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void addStructure(final IGridStructure structure) {
		ManagedReference<IGridStructure> refStructure = 
				AppContext.getDataManager().createReference(structure);

		worlds.put(structure.getIdWorld(), refStructure);
	}

	/**
	 * Retorna la instancia singleton de la clase.
	 * 
	 * @return la unica instancia
	 * @generated "De UML a Java V5.0 
	 * 		(com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public static GridManager getInstance() {
		if (instance == null) {
			instance = new GridManager();
		}
		return instance;
	}

	/**
	 * Remueve de la tabla de hash la estructura asociada al identificador
	 * pasado por parametro.
	 * 
	 * @param id identificador de la estructura a eliminar.
	 * @generated "De UML a Java V5.0 
	 * 		(com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void removeStructure(final Object id) {
		worlds.remove(id);
	}
}
