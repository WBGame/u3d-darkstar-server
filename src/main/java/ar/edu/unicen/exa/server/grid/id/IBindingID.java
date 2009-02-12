package ar.edu.unicen.exa.server.grid.id;

/**
 * Interface que deben implementar todas las clases que tienen ID que será 
 * creado utilizando {@link IDManager}. <br/>
 * Esta implementación permite que la instancia concreta de 
 * {@link IIDGenerator} utilizada pueda setear el id correspondiente 
 * y pueda armar el nombre que se utilizará en el binding 
 * de {@link com.sun.sgs.app.DataManager}.
 * 
 * @author Sebastián Perruolo &lt;sebastianperruolo at gmail dot com &gt;
 * @encoding UTF-8.
 * 
 */
public interface IBindingID {
	/**
	 * Retorna el ID del objeto.
	 * @return el ID del objeto.
	 */
	long getId();
	
	/**
	 * Setea el ID del objeto. 
	 * @param anId ID del objeto a setear.
	 */
	void setId(long anId);
	
}
