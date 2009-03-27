package ar.edu.unicen.exa.server.grid;

import java.io.Serializable;

import ar.edu.unicen.exa.server.grid.id.IBindingID;

import com.jme.math.Vector3f;
import com.sun.sgs.app.ManagedObject;

/** 
 *  Es una coleccion de celdas({@link Cell}) dispuestas de alguna manera con el
 *  objetivo de particionar el espacio fisico del mundo al cual representa. 
 *  La estructura debe poseer un identificador unico que se corresponde uno a
 *  uno con el identificador del  {@code IGameState} del cliente. Es decir, 
 *  representa a un objeto  {@code IGameState} del cliente en el servidor.
 *  
 *  @author Sebastián Perruolo &lt;sebastianperruolo at gmail dot com &gt;
 *  @encoding UTF-8.
 *  
 */
public interface IGridStructure 
		extends Serializable, ManagedObject, IBindingID {
	/**
	 * Retorna el identificador del mundo.
	 * 
	 * @return el identificador del mundo. 
	 */
	String getIdWorld();

	/**
	 * Establece el identificador del mundo. Dicho identificador no debe
	 * repetirse entre las estructuras.
	 * 
	 * @param id identificador del mundo.
	 */
	void setIdWorld(String id);

	/**
	 * Retorna la celda({@link Cell}) que corresponde a la posicion pasada por 
	 * parametro.
	 * 
	 * @return La celda({@link Cell}) que le corresponde a la posicion pasada
	 *         por Parametro.
	 * 
	 * @param position Posicion a evaluar.
	 */
	Cell getCell(Vector3f position);

	/**
	 * Retorna la coleccion de aquellas celdas({@link Cell}) que son adyacentes
	 * a la celda({@link Cell}) pasada por parametro. Se debe proporcionar 
	 * tambien la posicion del {@link Player}, para poder determinar la zona 
	 * dentro de la celda({@link Cell}) en la que se encuentra, ya que puede 
	 * influir en la determinacion de cuales son las celdas adyacentes. 
	 * Ver documentacion del diseño sobre tratamiento de
	 * celdas para mas informacion.
	 * 
	 * @return las celdas({@link Cell}) adyacentes.
	 * 
	 * @param cell Celda({@link Cell}) de la cual se obtienen los adyacentes.
	 * @param position Posicion dentro de la celda({@link Cell}).
	 */
	Cell[] getAdjacents(Cell cell, Vector3f position);

	/**
	 * Retorna la celda({@link Cell}) inicial, es decir la celda en la que 
	 * aparece por defecto el {@link Player} cuando ingresa por primera vez al
	 * mundo.
	 * 
	 * @return la celda inicial.
	 */
	Cell getSpawnCell();
	
	/**
	 * Retorna la posicion inicial, es decir la posicion en la que aparece por
	 * defecto el {@link Player} cuando ingresa por primera vez al mundo.
	 * 
	 * @return la posicion inicial.
	 */
	Vector3f getSpawnPosition();
	
	/**
	 * Establece la posicion inicial, es decir la posicion en la que aparece
	 * por defecto el {@link Player} cuando ingresa por primera vez al mundo.
	 * 
	 * @param x coord eje x.
	 * @param y coord eje y.
	 * @param z coord eje z.
	 * 
	 */
	void setSpawnPosition(float x, float y, float z);
}
