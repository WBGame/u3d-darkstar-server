package ar.edu.unicen.exa.server;

import java.io.File;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import ar.edu.unicen.exa.server.grid.GridManager;
import ar.edu.unicen.exa.server.grid.IGridStructure;
import ar.edu.unicen.exa.server.grid.MatrixGridStructure;
/**
 * <b>Clase utilitaria</b> encargada de inicializar {@link GridManager} dada la
 * configuración en el archivo de propiedades que recibe. También
 * genera un archivo de salida indicando los ids de cada mundo.
 * 
 * 
 * @author Sebastián Perruolo &lt;sebastianperruolo at gmail dot com/&gt;
 *
 */
public final class StructureProperties {
	/** Logger. */
	private static final Logger LOGGER = 
		Logger.getLogger(StructureProperties.class.getName());
	
	/**
	 * Propiedades utilizadas.
	 */
	private static Properties properties = null;
	/**
	 * Prefijo utilizado para obtener los mundos definidos.
	 */
	private static final String PROPERTY_PREFIX = 
		"ar.edu.unicen.exa.server.world.";
	/**
	 * Separador utilizado para separar el nombre del mundo. 
	 */
	private static final String SEPARATOR = ".";
	/**
	 * Sufijo utilizado para obtener la cantidad de celdas a lo ancho.
	 */
	public static final String GRID_WIDTH = "grid.width";
	/**
	 * Sufijo utilizado para obtener la cantidad de celdas a lo alto.
	 */
	public static final String GRID_HEIGHT = "grid.height";
	/**
	 * Sufijo utilizado para obtener el ancho y alto de la celda.
	 */
	public static final String CELL_SIZE = "cell.size";
	/**
	 * Sufijo utilizado para obtener la posición original X en el mundo.
	 */
	public static final String SPAWN_POSITION_WORLD_X = "spawnX";
	/**
	 * Sufijo utilizado para obtener la posición original Y en el mundo.
	 */
	public static final String SPAWN_POSITION_WORLD_Y = "spawnY";
	/**
	 * Sufijo utilizado para obtener la posición original Y en el mundo.
	 */
	public static final String SPAWN_POSITION_WORLD_Z = "spawnZ";

	/**
	 * Se oculta la creación de instancias.
	 */
	private StructureProperties() { }
	
	/**
	 * Método que parsea las propiedades y arma un HashMap &lt;String, 
	 * HashMap&lt;String, String&gt;&gt; conteniendo como clave del HashMap
	 * principal el name del mundo y como clave: otro HashMap que contiene
	 * el nombre de una propiedad de ese mundo y su valor.
	 * 
	 * @param props Properties que definen los mundos.
	 * @return HashMap &lt;String, 
	 * 		HashMap&lt;String, String&gt;&gt; conteniendo como clave del HashMap
	 * 		principal el name del mundo y como clave: otro HashMap que contiene
	 * 		el nombre de una propiedad de ese mundo y su valor.
	 */
	private static HashMap<String, HashMap<String, String>> parseProperties(
			final Properties props) {
		HashMap<String, HashMap<String, String>> result 
				= new HashMap<String, HashMap<String, String>>();
		for (Object prop : props.keySet()) {
			String propKey = (String) prop;
			if (propKey.startsWith(PROPERTY_PREFIX)) {
				String worldId = getWorldId(propKey);
				HashMap<String, String> worldData = 
						new HashMap<String, String>();
				//Cargar tamaño del mundo
				worldData.put(GRID_WIDTH, props.getProperty(
						PROPERTY_PREFIX + worldId + SEPARATOR + GRID_WIDTH));
				worldData.put(GRID_HEIGHT, props.getProperty(
						PROPERTY_PREFIX + worldId + SEPARATOR + GRID_HEIGHT));
				worldData.put(CELL_SIZE, props.getProperty(
						PROPERTY_PREFIX + worldId + SEPARATOR + CELL_SIZE));
				//Cargar posición por defecto en el mundo
				worldData.put(SPAWN_POSITION_WORLD_X, props.getProperty(
						PROPERTY_PREFIX + worldId + SEPARATOR 
						+ SPAWN_POSITION_WORLD_X));
				worldData.put(SPAWN_POSITION_WORLD_Y, props.getProperty(
						PROPERTY_PREFIX + worldId + SEPARATOR 
						+ SPAWN_POSITION_WORLD_Y));
				worldData.put(SPAWN_POSITION_WORLD_Z, props.getProperty(
						PROPERTY_PREFIX + worldId + SEPARATOR 
						+ SPAWN_POSITION_WORLD_Z));
				result.put(worldId, worldData);
			}
		}
		return result;
	}
	/**
	 * Este método parsea el la propiedad recibida retornando sólo
	 * el nombre del mundo. Para esto utiliza 
	 * {@link StructureProperties#PROPERTY_PREFIX} y 
	 * {@link StructureProperties#SEPARATOR}.
	 * 
	 * @param property propiedad
	 * @return sólo el nombre del mundo
	 */
	private static String getWorldId(final String property) {
		String result = property.replace(PROPERTY_PREFIX, "");
		return result.substring(0, result.indexOf(SEPARATOR));
	}
	/**
	 * Este método inicializa el GridManager con las estructuras
	 * indicadas en las propiedades recibidas por parámetro.
	 * @param props propiedades que indican la estructura de cada mundo.
	 */
	public static void initGridManager(final Properties props) {
		properties = props;
		log("==== Inicializando GridManager ====", true);
		// Levantar mundos desde propiedades
		HashMap<String, HashMap<String, String>> mundos = 
				parseProperties(props);
		String defaultWorld = 
				props.getProperty("ar.edu.unicen.exa.server.defaultWorld");
		// Obtener la instancia de GridManager.
		GridManager gridManager = GridManager.getInstance();
		for (String worldName : mundos.keySet()) {
			HashMap<String, String> mundo = mundos.get(worldName);
			IGridStructure world = new MatrixGridStructure(
				Integer.parseInt(mundo.get(StructureProperties.GRID_WIDTH)),
				Integer.parseInt(mundo.get(StructureProperties.GRID_HEIGHT)),
				Integer.parseInt(mundo.get(StructureProperties.CELL_SIZE))
				);
			world.setSpawnPosition(
				Float.parseFloat(
						mundo.get(StructureProperties.SPAWN_POSITION_WORLD_X)),
				Float.parseFloat(
						mundo.get(StructureProperties.SPAWN_POSITION_WORLD_Y)),
				Float.parseFloat(
						mundo.get(StructureProperties.SPAWN_POSITION_WORLD_Z))
			);
			gridManager.addStructure(world);
			if (defaultWorld.equals(worldName)) {
				gridManager.setDefaultWorld(world.getIdWorld());
			}
			
			String logText = "Agregado " 
					+ fixLength(worldName, mundos.keySet())
					+ " con ID " + world.getIdWorld();
			LOGGER.info(logText);
			log(logText, false);
		}
		log("-----------------------------------\n"
				+ "Utilizando por defecto el mundo " 
				+ gridManager.getDefaultWorld(), false);
	}
	
	/**
	 * Este método imprime el texto recibido como parámetro en un archivo
	 * para poder saber cómo quedaron los IDs desde la última inicialización.
	 * 
	 * @param text texto a imprimir en el archivo
	 * @param clean true para eliminar el contenido del archivo.
	 */
	private static void log(final String text, final boolean clean) {
		FileWriter out = null;
		String fileName = 
				properties.getProperty("com.sun.sgs.app.root") 
				+ "/out.txt";
		try {
			File file = new File(fileName);
			out = new FileWriter(file, !clean);
			out.write(text + "\n");
			
		} catch (Exception e) { 
			LOGGER.log(Level.WARNING, "Error escribiendo en el archivo " 
					+ fileName, e); 
		} finally {
			try { out.close();  } catch (Exception e) {
				LOGGER.warning("Error cerrando el buffer del archivo "
						+ fileName);
			}
		}
	}
	
	/**
	 * Este método es sólo para imprimir "lindo" en el archivo. Agrega espacios
	 * en blanco al final de worldName hasta completar el nombre del worldName
	 * mas largo.
	 * 
	 * @param worldName Nombre del mundo
	 * @param worldNames Set de todos los nombres de mundo
	 * @return worldName con espacios en blanco al final
	 */
	private static String fixLength(final String worldName, 
			final Set<String> worldNames) {
		int length = 0;
		String resultWorldName = worldName;
		for (String aWorldName : worldNames) {
			if (length < aWorldName.length()) {
				length = aWorldName.length();
			}
		}
		while (resultWorldName.length() < length) {
			resultWorldName = resultWorldName + " ";
		}
		return resultWorldName;
	}
}
