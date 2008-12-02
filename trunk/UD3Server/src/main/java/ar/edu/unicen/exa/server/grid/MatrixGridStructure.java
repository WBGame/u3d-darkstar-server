/**
 * 
 */
package server.grid;

import com.jme.math.Vector3f;

/** 
 *  Es una coleccion de celdas dispuestas en forma de matriz con el objetivo de particionar el espacio fisico del mundo al cual representa en zonas iguales. Posee un identificador unico que se corresponde uno a uno con el identificador del IGameState del cliente. Es decir, representa a un objeto IGameState del cliente en el servidor.
 * @generated "De UML a Java V5.0 (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public class MatrixGridStructure implements IGridStructure {
	/** 
	 *  Identificador unico de la estructura.
	 * @generated "De UML a Java V5.0 (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	protected String idWorld;

	/** 
	 *  Representacion en forma de matriz de las celdas de la estructura.
	 * @uml.annotations for <code>structure</code>
	 *     collection_type="server.grid.Cell"
	 * @generated "De UML a Java V5.0 (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	protected Cell[][] structure;

	/**
	 * @param id
	 * @generated "De UML a Java V5.0 (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void setIdWorld(String id) {
		// begin-user-code
		// TODO Apéndice de método generado automáticamente

		// end-user-code
	}

	/**
	 * @return
	 * @generated "De UML a Java V5.0 (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public String getIdWorld() {
		// begin-user-code
		// TODO Apéndice de método generado automáticamente
		return null;
		// end-user-code
	}

	/**
	 * @param position
	 * @return
	 * @generated "De UML a Java V5.0 (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public Cell getCell(Vector3f position) {
		// begin-user-code
		// TODO Apéndice de método generado automáticamente
		return null;
		// end-user-code
	}

	/**
	 * @return
	 * @generated "De UML a Java V5.0 (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public Cell getSpawnCell() {
		// begin-user-code
		// TODO Apéndice de método generado automáticamente
		return null;
		// end-user-code
	}

	/**
	 * @param cell
	 * @param position
	 * @return
	 * @generated "De UML a Java V5.0 (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public Cell[] getAdjacents(Cell cell, Vector3f position) {
		// begin-user-code
		// TODO Apéndice de método generado automáticamente
		return null;
		// end-user-code
	}

	/**
	 * @param spawnCell
	 * @generated "De UML a Java V5.0 (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void setSpawnCell(Cell spawnCell) {
		// begin-user-code
		// TODO Apéndice de método generado automáticamente

		// end-user-code
	}
}