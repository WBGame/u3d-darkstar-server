package ar.edu.unicen.exa.server.grid.id;

import java.util.logging.Logger;

/**
 * Implementación de la interface {@link IIDGenerator}.
 * Esta implementación genera ID consecutivos (1, 2, 3, 4, ...).
 * 
 * @author Sebastián Perruolo &lt;sebastianperruolo at gmail dot com &gt;
 * @encoding UTF-8.
 */

public class SecuenceIDGeneratorImpl implements IIDGenerator {
	/** Logger. */
	private static Logger logger = 
		Logger.getLogger(SecuenceIDGeneratorImpl.class.getName());

	/**  Para cumplir con la version de la clase Serializable. */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Separador que se utilizará para generar el binding name.
	 */
	private static final String SEPARATOR = "_"; 
	
	/**
	 * Atributo que almacena el próximo ID a utilizar.
	 */
	private long nextId;
	
	/**
	 * Creador de nueva instancia de esta clase.
	 * 
	 * @param start número por el cual empezar la numeración.
	 */
	public SecuenceIDGeneratorImpl(final long start) {
		this.nextId = start;
	}

	/**
	 * Este método genera un nuevo ID y se lo setea al objeto que está
	 * recibiendo por parámetro, gracias a que este objeto implementa
	 * {@link IBindingID}. 
	 * El ID es generado secuencialmente (1, 2, 3, 4, ...).
	 * 
	 * @param o Objecto al que se le seteará el nuevo ID.
	 */
	@Override
	public final void setNewID(final IBindingID o) {
		o.setId(nextId);
		nextId++;
	}
	
	/**
	 * Este método recibe una instancia y genera el nombre que se utilizará
	 * para hacer el binding con el {@link com.sun.sgs.app.DataManager}.
	 * El nombre se arma concatenando el nombre de la clase del objeto,
	 * un separador (normalmente _ ) y el ID del objeto.
	 * 
	 * @param o instancia a la que se debe generar el nombre.
	 * @return el nombre que se debe usar para hacer el store del objeto.
	 */
	@Override
	public final String getBindingName(final IBindingID o) {
		//TODO corregir la siguiente línea
		String abstraction = resolveAbstraction(o.getClass().getName());
		StringBuffer result = new StringBuffer();
		result.append(abstraction);
		result.append(SEPARATOR);
		result.append(o.getId());
		logger.info("Se resolvio el binding name " + result.toString() 
				+ " para el id " + o.getId());
		return result.toString();
	}
	
	/**
	 * Este método recibe una clase y genera el nombre que se utilizó
	 * para almacenar la instancia de esa clase dado el ID que se recibe.
	 * El nombre se arma concatenando el nombre de la clase del objeto,
	 * un separador (normalmente _ ) y el ID del objeto.
	 * 
	 * @param class1 Clase del objeto que se quiere recuperar.
	 * @param id ID del objeto que se quiere recuperar.
	 * @return el nombre que se utilizó para hacer el store del objeto.
	 */
	@Override
	public final String getBindingName(final Class< ? > class1, final long id) {
		StringBuffer result = new StringBuffer();
		result.append(class1.getName());
		result.append(SEPARATOR);
		result.append(id);
		logger.info("Se resolvio el binding name " + result.toString() 
				+ " para el id " + id);
		return result.toString();
	}
	/**
	 * Se resuelven jerarquias estáticamente.
	 * @param className Clase a evaluar.
	 * @return una super clase si es necesario.
	 */
	private static String resolveAbstraction(final String className) {
		if ("ar.edu.unicen.exa.server.grid.MatrixGridStructure"
				.equals(className)) {
			return "ar.edu.unicen.exa.server.grid.IGridStructure";
		}
		return className;
	}

	/**
	 * Este método recibe una clase y genera el nombre que se utilizó
	 * para almacenar la instancia de esa clase dado el ID que se recibe.
	 * El nombre se arma concatenando el nombre de la clase del objeto,
	 * un separador (normalmente _ ) y el ID del objeto.
	 * Si se recibe un nombre (el String contiene _) entonces se retorna
	 * ese nombre que se recibió.
	 * 
	 * @param class1 Clase del objeto que se quiere recuperar.
	 * @param idOrName ID del objeto que se quiere recuperar.
	 * @return el nombre que se utilizó para hacer el store del objeto.
	 */
	@Override
	public final String getBindingName(final Class< ? > class1, 
			final String idOrName) {
		if (idOrName.contains(SEPARATOR)) {
			return idOrName;
		}
		long id = Long.parseLong(idOrName);
		return getBindingName(class1, id);
	}
}
