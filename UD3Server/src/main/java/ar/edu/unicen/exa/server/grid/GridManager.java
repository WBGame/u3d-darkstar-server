package ar.edu.unicen.exa.server.grid;

import java.io.Serializable;
import com.sun.sgs.app.AppContext;
import com.sun.sgs.app.DataManager;
import com.sun.sgs.app.ManagedObject;
import com.sun.sgs.app.ObjectNotFoundException;
import com.sun.sgs.app.TransactionException;
//TODO Corregir los comentarios de toda la clase porque no representan lo que hace el codigo internamente.
//TODO Uniformizar el codigo para el tratamiento de las excepciones porque en todos 
//los casos es distinto.
//Al mismo tiempo definir si se va a retornar null en caso de que los objetos no 
//se puedan recuperar del object store, o si se va a hacer un rethrow de las 
//excepciones que genera el framework.
/** 
 *  Contiene referencias a todas las  {@link IGridStructure} de celdas. Se 
 *  pueden agregar estructuras, remover estructuras como asi tambien obtenerla
 *  una estructura dado el identificador del mundo.
 *  
 */
public final class GridManager implements Serializable, ManagedObject {
	/** The version of the serialized form of this class. */
	private static final long serialVersionUID = -7374467626346407012L;

	/** 
	 *  Es una tabla de hash que contiene referencias  {@code ManagedReference}
	 *  a las estructuras que mantiene.
	 *  
	 */
	//XXX no se hizo uso de la tabla de hash debido a que se generaban
	//excepciones cuando se recuperaban los mundos
	//almacenar los mundos por medio de su id utilizando el metodo setBinding()
	//del DataManger y recuperalo con getBinding()
	//private Hashtable<Long, ManagedReference<IGridStructure>> worlds = null;

	/** 
	 *  Instancia unica singleton de la clase.
	 */
	private static GridManager instance = null; 
	
	//XXX agregado para la generacion de id's unicos para los mundos 
	/** Almacena el proximo identificador de mundo a generar. */
    private long nextWorldID;

	/**
	 * Metodo privado para implementar un singleton.
	 */
	private GridManager() {
		//worlds = new Hashtable<Long, ManagedReference<IGridStructure>>();
		
		nextWorldID = 0L;
	}
	/**
	 * Retorna la estructura que se corresponde con el
	 * identificador pasado por parametro.
	 * 
	 * @return estructura que se corresponde con el identificador
	 * 
	 * @param id identificador de la estructura a obtener
	 */
	public IGridStructure getStructure(final String id) {
		
		DataManager dataManager = AppContext.getDataManager();
		IGridStructure grid = null;
		try {
			String name = IGridStructure.class.getName() + "_" + id;
			grid = (IGridStructure) dataManager.getBinding(name);
		} catch (Exception e) {
			return null;
		}

		return grid;
	}

	/**
	 * Agrega a la tabla de hash la estructura pasada por parametro. Para esto
	 * debe encargarse de crear una {@code ManagedReference} invocando al
	 * metodo {@code createReference()} del {@code DataManager}.
	 * 
	 * @param structure estructura a almacenar
	 */
	public void addStructure(final IGridStructure structure) {
		
		DataManager dataManager = AppContext.getDataManager();
		dataManager.markForUpdate(this);
		nextWorldID++;
		String worldID = String.valueOf(nextWorldID);
		structure.setIdWorld(worldID);
		String name = IGridStructure.class.getName() + "_" + worldID;
		try {
			dataManager.setBinding(name, structure);
		} catch (IllegalArgumentException iae) {
			iae.printStackTrace();
		} catch (ObjectNotFoundException onfe) {
			onfe.printStackTrace();
		} catch (TransactionException te) {
			te.printStackTrace();
		}
	}

	/**
	 * Retorna la instancia singleton de la clase.
	 * 
	 * @return la unica instancia
	 * @generated "De UML a Java V5.0 
	 * 		(com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public static GridManager getInstance() {
		//XXX parte de la inicializacion del sistema.
		//modificacion del metodo para levantar el GridManager del Object Store
		if (instance == null) {
			DataManager d = AppContext.getDataManager();
			String name = GridManager.class.getName();
			try {	
				// recupero el GridManager a partir del nombre de la clase.
				// esto puede lanzar una excepcion que indica la ausencia 
				// del objeto, entonces se debe crear una nueva instancia de
				// GridManager.
				instance = (GridManager) d.getBinding(name);
				System.out.println(
						"Se ha recuperado la instancia de GridManager");
			} catch (Exception e) {
				// creo un GridManager 
				instance = new GridManager();
				// registro la instancia dentro del Object Store.
				d.setBinding(name , instance);
				System.out.println(
						"Se ha creado una nueva instancia de GridManager");
			}
		}		
		return instance;
	}

	/**
	 * Remueve de la tabla de hash la estructura asociada al identificador
	 * pasado por parametro.
	 * 
	 * @param id identificador de la estructura a eliminar.
	 */
	public void removeStructure(final String id) {
		//XXX modificacion del metodo para la eliminacion de un objeto dentro
		//del object store
		String name = IGridStructure.class.getName() + "_" + id;
		DataManager dataManager = AppContext.getDataManager();
		dataManager.markForUpdate(this);
		dataManager.removeBinding(name);
	}
}
