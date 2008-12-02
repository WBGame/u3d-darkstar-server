/**
 * 
 */
package server.grid;

import java.io.Serializable;
import com.sun.sgs.app.ManagedObject;
import com.jme.math.Vector3f;

/** 
 *  Es una coleccion de celdas dispuestas de alguna manera con el objetivo de particionar el espacio fisico del mundo al cual representa. La estructura debe poseer un identificador unico que se corresponde uno a uno con el identificador del  {@code IGameState} del cliente. Es decir, representa a un objeto  {@code IGameState} del cliente en el servidor.
 * @generated "De UML a Java V5.0 (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public interface IGridStructure extends Serializable, ManagedObject {
	/**
	 * Retorna el identificador del mundo.
	 * @return 
	 * @generated "De UML a Java V5.0 (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public String getIdWorld();

	/**
	 * Establece el identificador del mundo. Dicho identificador no debe repetirse entre las estructuras.
	 * @param id 
	 * @generated "De UML a Java V5.0 (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void setIdWorld(String id);

	/**
	 * Retorna la   {@link Cell} que corresponde a la posicion pasada por parametro.
	 * @return 
	 * @param position 
	 * @generated "De UML a Java V5.0 (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public Cell getCell(Vector3f position);

	/**
	 * Retorna la celda inicial, es decir la celda en la que aparece por defecto el jugador cuando ingresa por primera vez al mundo.
	 * @return 
	 * @generated "De UML a Java V5.0 (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public Cell getSpawnCell();

	/**
	 * Retorna la coleccion de aquellas celdas que son adyacentes a la celda pasada por parametro. Se debe proporcionar tambien la posicion del jugador, para poder determinar la zona dentro de la celda en la que se encuentra, ya que puede influir en la determinacion de cuales son las celdas adyacentes. Ver documentacion del diseño sobre tratamiento de celdas para mas informacion.
	 * @return 
	 * @param cell 
	 * @param position 
	 * @generated "De UML a Java V5.0 (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public Cell[] getAdjacents(Cell cell, Vector3f position);

	/**
	 * Establece la celda inicial, es decir la celda en la que aparece por defecto el jugador cuando ingresa por primera vez al mundo.
	 * @param spawnCell 
	 * @generated "De UML a Java V5.0 (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void setSpawnCell(Cell spawnCell);
}