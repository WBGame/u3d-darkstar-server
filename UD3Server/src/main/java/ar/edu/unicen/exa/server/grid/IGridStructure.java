package ar.edu.unicen.exa.server.grid;

import java.io.Serializable;
import com.sun.sgs.app.ManagedObject;
import com.jme.math.Vector3f;

/** 
 *  Es una coleccion de celdas dispuestas de alguna manera con el objetivo de
 *  particionar el espacio fisico del mundo al cual representa. La estructura
 *  debe poseer un identificador unico que se corresponde uno a uno con el
 *  identificador del  {@code IGameState} del cliente. Es decir, representa
 *  a un objeto  {@code IGameState} del cliente en el servidor.
 *  
 * @generated "De UML a Java V5.0 
 * 		(com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public interface IGridStructure extends Serializable, ManagedObject {
	/**
	 * Retorna el identificador del mundo.
	 * @return el identificador del mundo. 
	 * @generated "De UML a Java V5.0 
	 * 		(com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	String getIdWorld();

	/**
	 * Establece el identificador del mundo. Dicho identificador no debe
	 * repetirse entre las estructuras.
	 * 
	 * @param id identificador del mundo
	 * @generated "De UML a Java V5.0 
	 * 		(com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	void setIdWorld(String id);

	/**
	 * Retorna la {@link Cell} que corresponde a la posicion pasada por 
	 * parámetro.
	 * 
	 * @return la {@link Cell} que corresponde
	 * @param position posición a evaluar
	 * @generated "De UML a Java V5.0 
	 * 		(com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	Cell getCell(Vector3f position);

	/**
	 * Retorna la celda inicial, es decir la celda en la que aparece por
	 * defecto el jugador cuando ingresa por primera vez al mundo.
	 * 
	 * @return la celda inicial
	 * @generated "De UML a Java V5.0 
	 * 		(com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	Cell getSpawnCell();

	/**
	 * Retorna la coleccion de aquellas celdas que son adyacentes a la celda
	 * pasada por parametro. Se debe proporcionar tambien la posicion del
	 * jugador, para poder determinar la zona dentro de la celda en la que se
	 * encuentra, ya que puede influir en la determinacion de cuales son las
	 * celdas adyacentes. Ver documentacion del diseñoo sobre tratamiento de
	 * celdas para mas informacion.
	 * 
	 * @return las celdas adyacentes.
	 * @param cell celda de la cual obtener los adyacentes.
	 * @param position posición dentro de la celda.
	 * @generated "De UML a Java V5.0 
	 * 		(com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	Cell[] getAdjacents(Cell cell, Vector3f position);

	/**
	 * Establece la celda inicial, es decir la celda en la que aparece por
	 * defecto el jugador cuando ingresa por primera vez al mundo.
	 * 
	 * @param spawnCell celda inicial
	 * @generated "De UML a Java V5.0
	 * 		(com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	void setSpawnCell(Cell spawnCell);
}
