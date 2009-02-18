package ar.edu.unicen.exa.server.grid;

import java.io.Serializable;
import java.util.logging.Logger;

import ar.edu.unicen.exa.server.grid.id.IDManager;

import com.sun.sgs.app.AppContext;
import com.sun.sgs.app.DataManager;
import com.sun.sgs.app.ManagedObject;
import com.sun.sgs.app.NameNotBoundException;
import com.sun.sgs.app.ObjectNotFoundException;

//TODO Uniformizar el codigo para el tratamiento de las excepciones porque en
//todos los casos es distinto.
//Al mismo tiempo definir si se va a retornar null en caso de que los objetos
//no se puedan recuperar del object store, o si se va a hacer un rethrow de las 
//excepciones que genera el framework.
/** 
 * Manager de {@link IGridStructure} (Manager de los diferentes mundos). Se 
 * pueden agregar estructuras, remover, como tambien asi obtener una, dado el 
 * identificador de la misma.
 * 
 * @encoding UTF-8.
 */
public final class GridManager implements Serializable, ManagedObject {

	/**  Para cumplir con la version de la clase {@Serializable}. */
	private static final long serialVersionUID = -7374467626346407012L;

	/** Logger. */
	private static Logger logger = 
		Logger.getLogger(GridManager.class.getName());

	/** Instancia unica singleton de la clase. */
	private static GridManager instance = null; 

	/** Binding name del mundo por defecto. */
	private String defaultWorld = null; 

	/**
	 * Getter para recuperar el binding name del mundo por defecto.
	 * 
	 * @return binding names
	 */
	public String getDefaultWorld() {
		return defaultWorld;
	}

	/**
	 * Seteador para inicializar el mundo por defecto.
	 * 
	 * @param name binding name
	 */
	public void setDefaultWorld(final String name) {
		AppContext.getDataManager().markForUpdate(this);
		this.defaultWorld = name;
	}

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
			String name = IDManager.getBindingName(
					IGridStructure.class, 
					id
				);
			grid = (IGridStructure) dataManager.getBinding(name);
		} catch (NameNotBoundException e) {
			e.printStackTrace();
		}

		return grid;
	}

	/**
	 * Agrega al {@link DataManager} la estructura establecida como parámetro.
	 * 
	 * Para esto debe encargarse de crear una {@code ManagedReference} invocando
	 * al método {@code createReference()} del {@code DataManager}.
	 * 
	 * @param structure estructura a almacenar.
	 */
	public void addStructure(final IGridStructure structure) {

		DataManager dataManager = AppContext.getDataManager();
		dataManager.markForUpdate(this);

		IDManager.setNewID(structure);
		String name = IDManager.getBindingName(structure);
		try {
			logger.info("Agregando estructura dentro del GridManager. Id de "
					+ "mundo: " + name);
			dataManager.setBinding(name, structure);
		} catch (ObjectNotFoundException e) {
			e.printStackTrace();
		}
		
		if (defaultWorld == null) {
			setDefaultWorld(structure.getIdWorld());
		}
	}

	/**
	 * Retorna la instancia de la clase. En caso de existir, se recupera via 
	 * getBinding y asi recuperar el estado del manager con los mundos
	 * ({@link IGridStructure}) previamente almacenados. Si no se encuentra 
	 * almacenado, significa que se ejecuta por primera vez, entonces se crea
	 * una instancia y se guarda con el metodo setBinding.
	 * 
	 * @return GridManager Retorna la instancia de la clase.
	 */
	public static GridManager getInstance() {
		if (instance == null) {
			DataManager d = AppContext.getDataManager();
			String name = GridManager.class.getName();
			try {	
				instance = (GridManager) d.getBinding(name);
				logger.info("Se ha recuperado la instancia de GridManager");
			} catch (NameNotBoundException e) {
				// Creo un GridManager. 
				instance = new GridManager();
				// Registro la instancia dentro del Object Store.
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
		String name = IDManager.getBindingName(
				IGridStructure.class, 
				id
			);
		DataManager dataManager = AppContext.getDataManager();
		dataManager.removeBinding(name);
	}
	
	/**
	 * Retorna la estructura por defecto dependiendo del identificador
	 * definido en el atributo DEFAULT_WORLD.
	 * 
	 * @return el mundo por defecto
	 */
	public IGridStructure getDefaultStructure() {
		return this.getStructure(defaultWorld);
	}
}
