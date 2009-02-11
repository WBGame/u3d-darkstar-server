package ar.edu.unicen.exa.server.grid.id;

import java.io.Serializable;

import com.sun.sgs.app.ManagedObject;
/**
 * Interfaz que define los métodos que deberá implementar la
 * clase que genere ID.
 * 
 * @author Sebastián Perruolo &lt;sebastianperruolo at gmail dot com &gt;
 */
public interface IIDGenerator extends Serializable, ManagedObject {
	
	/**
	 * Este método genera un nuevo ID y se lo setea al objeto que está
	 * recibiendo por parámetro, gracias a que este objeto implementa
	 * {@link IBindingID}.
	 * @param o Objecto al que se le seteará el nuevo ID
	 */
	void setNewID(IBindingID o);
	/**
	 * Este método recibe una instancia y genera el nombre que se utilizará
	 * para hacer el binding con el {@link com.sun.sgs.app.DataManager}.
	 * @param o instancia a la que se debe generar el nombre
	 * @return el nombre que se debe usar para hacer el store del objeto.
	 */
	String getBindingName(IBindingID o);
	
	/**
	 * Este método recibe una clase y genera el nombre que se utilizó
	 * para almacenar la instancia de esa clase dado el ID que se recibe.
	 * 
	 * @param class1 Clase del objeto que se quiere recuperar.
	 * @param id ID del objeto que se quiere recuperar.
	 * @return el nombre que se utilizó para hacer el store del objeto.
	 */
	String getBindingName(Class< ? > class1, long id);
	
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
	String getBindingName(Class< ? > class1, String idOrName);

	
	
}
