package ar.edu.unicen.exa.server.grid.id;

import com.sun.sgs.app.AppContext;
import com.sun.sgs.app.NameNotBoundException;
/**
 * Esta clase da flexibilidad para cambiar la implementación de 
 * {@link IIDGenerator} que se utiliza en el server.
 * Si se desea cambiar la implementación de {@link IIDGenerator}
 * hay que modificar el método {@link IDManager#newInstance()}. <br/>
 * También ejecuta la persistencia de esta implementación para
 * que no se pierda el conteo de ID.
 * 
 * @author Sebastián Perruolo &lt;sebastianperruolo at gmail dot com &gt;
 * @encoding UTF-8
 */
public class IDManager {
	/**
	 * Instancia de {@link IIDGenerator} que se utilizará.
	 */
	private static IIDGenerator instance = null;
	
	/**
	 * Nombre con el que se persistirá la instancia de {@link IIDGenerator} en
	 * el DataManager.
	 */
	private static final String INSTANCE_BINDING_NAME = "IDGeneratorInstance";
	
	/**
	 * Se hace invisible el método de creación. 
	 */
	protected IDManager() { }

	/**
	 * Este método genera un nuevo ID y se lo setea al objeto que está
	 * recibiendo por parámetro, gracias a que este objeto implementa
	 * {@link IBindingID}.
	 * @param o Objecto al que se le seteará el nuevo ID
	 */
	public static void setNewID(final IBindingID o) {
		try {
			instance = (IIDGenerator) AppContext.getDataManager()
					.getBinding(INSTANCE_BINDING_NAME);
        } catch (NameNotBoundException ex) {
        	instance = newInstance();
        	AppContext.getDataManager()
        			.setBinding(INSTANCE_BINDING_NAME, instance);
        }
        AppContext.getDataManager().markForUpdate(instance);
        instance.setNewID(o);
	}
	
	/**
	 * Este método recibe una instancia y genera el nombre que se utilizará
	 * para hacer el binding con el {@link com.sun.sgs.app.DataManager}.
	 * @param o instancia a la que se debe generar el nombre
	 * @return el nombre que se debe usar para hacer el store del objeto.
	 */
	public static String getBindingName(final IBindingID o) {
		try {
			instance = (IIDGenerator) AppContext.getDataManager()
					.getBinding(INSTANCE_BINDING_NAME);
        } catch (NameNotBoundException ex) {
        	instance = newInstance();
        	AppContext.getDataManager()
        			.setBinding(INSTANCE_BINDING_NAME, instance);
        }
        AppContext.getDataManager().markForUpdate(instance);
        return instance.getBindingName(o);
	}

	/**
	 * Este método recibe una clase y genera el nombre que se utilizó
	 * para almacenar la instancia de esa clase dado el ID que se recibe.
	 * 
	 * @param class1 Clase del objeto que se quiere recuperar.
	 * @param id ID del objeto que se quiere recuperar.
	 * @return el nombre que se utilizó para hacer el store del objeto.
	 */
	public static String getBindingName(
			final Class< ? > class1, final long id) {

		try {
			instance = (IIDGenerator) AppContext.getDataManager()
					.getBinding(INSTANCE_BINDING_NAME);
        } catch (NameNotBoundException ex) {
        	instance = newInstance();
        	AppContext.getDataManager()
        			.setBinding(INSTANCE_BINDING_NAME, instance);
        }
        AppContext.getDataManager().markForUpdate(instance);
        return instance.getBindingName(class1, id);
	}
	
	/**
	 * Este método recibe una clase y genera el nombre que se utilizó
	 * para almacenar la instancia de esa clase dado el ID (o name) que se 
	 * recibe.<br/>
	 * El ID, a diferencia de {@link IDManager#getBindingName(Class, long)},
	 * puede ser un nombre, en cuyo caso se retorna este nombre directamente.
	 * 
	 * @param class1 Clase del objeto que se quiere recuperar.
	 * @param idOrName ID del objeto que se quiere recuperar.
	 * @return el nombre que se utilizó para hacer el store del objeto.
	 */
	public static String getBindingName(
			final Class< ? > class1, final String idOrName) {
		try {
			instance = (IIDGenerator) AppContext.getDataManager()
					.getBinding(INSTANCE_BINDING_NAME);
        } catch (NameNotBoundException ex) {
        	instance = newInstance();
        	AppContext.getDataManager()
        			.setBinding(INSTANCE_BINDING_NAME, instance);
        }
        AppContext.getDataManager().markForUpdate(instance);
        return instance.getBindingName(class1, idOrName);
	}
	
	/**
	 * Este método crea la instancia del {@link IIDGenerator}
	 * que se utilizará.
	 * 
	 * @return instancia de {@link IIDGenerator}
	 */
	private static IIDGenerator newInstance() {
		return new SecuenceIDGeneratorImpl(0L);
	}
}
