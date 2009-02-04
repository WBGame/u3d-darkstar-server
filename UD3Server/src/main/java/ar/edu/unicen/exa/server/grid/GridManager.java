package ar.edu.unicen.exa.server.grid;

import java.io.Serializable;
import com.sun.sgs.app.AppContext;
import com.sun.sgs.app.DataManager;
import com.sun.sgs.app.ManagedObject;
import com.sun.sgs.app.ObjectNotFoundException;
import com.sun.sgs.app.TransactionException;

//TODO Uniformizar el codigo para el tratamiento de las excepciones porque en
//todos los casos es distinto.
//Al mismo tiempo definir si se va a retornar null en caso de que los objetos
//no se puedan recuperar del object store, o si se va a hacer un rethrow de las 
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
	 * Almacena la estructura pasada por parametro. Para esto, se genera un id
	 * de mundo unico y ademas se crea un nombre de mundo el cual esta formado 
	 * por el nombre de la clase con los paquetes a la cual pertenece y el id 
	 * del mismo. Finalmente por medio del {@link DataManahger} se guarda la
	 * instancia del nuevo mundo con el metodo setBinding(). 
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
	 * Retorna la instancia singleton de la clase. En caso de no existir, 
	 * se recupera via getBinding y asi recuperar el estado del manager
	 * con los mundos previamente almacenados. Si no se encuentra almacenado,
	 * significa que se ejecuta por primera vez, entonces se crea una instancia
	 *  y se guarda con el metodo setBinding.
	 * 
	 * @return la unica instancia
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
	 * Remueve de la estructura asociada al identificador
	 * pasado por parametro.
	 * 
	 * @param id identificador de la estructura a eliminar.
	 */
	public void removeStructure(final String id) {
		String name = IGridStructure.class.getName() + "_" + id;
		DataManager dataManager = AppContext.getDataManager();
		dataManager.removeBinding(name);
	}
}
