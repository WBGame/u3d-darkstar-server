package ar.edu.unicen.exa.server.grid;

import java.io.Serializable;
import java.util.logging.Logger;

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
 * Manager de {@link IGridStructure} (Manager de los diferentes mundos). Se 
 * pueden agregar estructuras, remover, como asi tambien obtener una dado el 
 * identificador de la misma.
 * 
 * @encoding UTF-8
 */
public final class GridManager implements Serializable, ManagedObject {

	/** The version of the serialized form of this class. */
	private static final long serialVersionUID = -7374467626346407012L;

	/** Logger. */
	private static Logger logger = 
		Logger.getLogger(GridManager.class.getName());

	/** Instancia unica singleton de la clase. */
	private static GridManager instance = null; 

	/** Almacena el proximo identificador de mundo a generar. */
	private long nextWorldID = 0L;

	/** Metodo privado para implementar un singleton. */
	private GridManager() { }

	/**
	 * Retorna la estructura que se corresponde con el identificador pasado por 
	 * parametro.
	 *  
	 * @param id identificador de estructura.
	 * 
	 * @return IGridStructure estructura asociada al parametro.
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
	 * Agrega al {@link DataManager} la estructura establecida como parámetro.
	 * 
	 * Para esto debe encargarse de crear una {@code ManagedReference} invocando
	 * al método {@code createReference()} del {@code DataManager}.
	 * 
	 * @param structure estructura a almacenar
	 */
	public void addStructure(final IGridStructure structure) {

		DataManager dataManager = AppContext.getDataManager();
		dataManager.markForUpdate(this);

		//TODO fix it. ID generation must be in another place.
		nextWorldID++;
		String worldID = String.valueOf(nextWorldID);
		structure.setIdWorld(worldID);
		String name = IGridStructure.class.getName() + "_" + worldID;

		try {
			logger.info("Agregando estructura dentro del GridManager. Id de "
					+ "mundo: " + worldID);
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
	 * Retorna la instancia de la clase. En caso de no existir, 
	 * se recupera via getBinding y asi recuperar el estado del manager
	 * con los mundos previamente almacenados. Si no se encuentra almacenado,
	 * significa que se ejecuta por primera vez, entonces se crea una instancia
	 * y se guarda con el metodo setBinding.
	 * 
	 * @return GridManager
	 */
	public static GridManager getInstance() {
		if (instance == null) {
			DataManager d = AppContext.getDataManager();
			String name = GridManager.class.getName();
			try {	
				instance = (GridManager) d.getBinding(name);
				logger.info("Se ha recuperado la instancia de GridManager");
			} catch (Exception e) {
				// creo un GridManager 
				instance = new GridManager();
				// registro la instancia dentro del Object Store.
				d.setBinding(name , instance);
				logger.info("Se ha creado una nueva instancia del GridManager");
			}
		}	
		return instance;
	}

	/**
	 * Remueve la estructura asociada al identificador definido por parámetro. 
	 * 
	 * @param id identificador de la estructura a eliminar.
	 */
	public void removeStructure(final String id) {
		//TODO fix it. ID generation must be in another place.
		String name = IGridStructure.class.getName() + "_" + id;
		DataManager dataManager = AppContext.getDataManager();
		dataManager.removeBinding(name);
	}
}
