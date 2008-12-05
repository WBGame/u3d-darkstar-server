package ar.edu.unicen.exa.server.grid;

import com.jme.math.Vector3f;

/** 
 *  Es una coleccion de celdas dispuestas en forma de matriz con el objetivo de
 *  particionar el espacio fisico del mundo al cual representa en zonas
 *  iguales. Posee un identificador unico que se corresponde uno a uno con el
 *  identificador del IGameState del cliente. Es decir, representa a un objeto
 *  IGameState del cliente en el servidor.
 *  
 * @generated "De UML a Java V5.0 
 * 		(com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public class MatrixGridStructure implements IGridStructure {
	/** The version of the serialized form of this class. */
	private static final long serialVersionUID = 3105473591463699480L;
	
	/** 
	 * Identificador unico de la estructura.
	 * @generated "De UML a Java V5.0
	 * 		(com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private String idWorld;

	/** 
	 *  Representacion en forma de matriz de las celdas de la estructura.
	 * @uml.annotations for <code>structure</code>
	 *     collection_type="server.grid.Cell"
	 * @generated "De UML a Java V5.0 
	 * 		(com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private Cell[][] structure;
	
	/**
	 * //TODO javadoc.
	 * @param anStructure .
	 */
	public MatrixGridStructure(final Cell[][] anStructure) {
		structure = anStructure;
	}
	
	/**
	 * Setea el identificador único de la estructura.
	 * @param id identificador único.
	 * @generated "De UML a Java V5.0 
	 * 		(com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public final void setIdWorld(final String id) {
		this.idWorld = id;
	}

	/**
	 * Retorna el identificador único de la estructura.
	 * @return identificador único.
	 * @generated "De UML a Java V5.0 
	 * 		(com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public final String getIdWorld() {
		return this.idWorld;
	}

	/**
	 * Retorna la celda que contiene la posición "position".
	 * 
	 * @param position posición a evaluar.
	 * @return la celda que contiene la posición "position". <code>null</code>
	 * si la posición "position" está fuera de la grilla.
	 * @generated "De UML a Java V5.0 
	 * 		(com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public final Cell getCell(final Vector3f position) {
		for (int i = 0; i < structure.length; i++) {
			for (int j = 0; j < structure[i].length; j++) {
				if (structure[i][j].isInside(position)) {
					return structure[i][j];
				}
			}
		}
		return null;
	}

	/**
	 * Retorna la celda inicial, es decir la celda en la que aparece por
	 * defecto el jugador cuando ingresa por primera vez al mundo.
	 * 
	 * @return la celda inicial.
	 * @generated "De UML a Java V5.0 
	 * 		(com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public final Cell getSpawnCell() {
		return structure[0][0];
	}

	/**
	 * Retorna la coleccion de aquellas celdas que son adyacentes a la celda
	 * pasada por parametro. Se debe proporcionar tambien la posicion del
	 * jugador, para poder determinar la zona dentro de la celda en la que se
	 * encuentra, ya que puede influir en la determinacion de cuales son las
	 * celdas adyacentes. Ver documentacion del diseñoo sobre tratamiento de
	 * celdas para mas informacion.
	 * 
	 * @param cell celda de la cual obtener los adyacentes.
	 * @param position posición dentro de la celda.
	 * @return un vector vacío ya que esta implementación contiene una sola 
	 * 		celda.
	 * @generated "De UML a Java V5.0 
	 * 		(com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public final Cell[] getAdjacents(final Cell cell, final Vector3f position) {
		Cell[] result = new Cell[0];
		return result;
	}

	/**
	 * Establece la celda inicial, es decir la celda en la que aparece por
	 * defecto el jugador cuando ingresa por primera vez al mundo.
	 * 
	 * @param spawnCell celda inicial
	 * @generated "De UML a Java V5.0 
	 * 		(com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void setSpawnCell(final Cell spawnCell) {
		// begin-user-code
		// TODO Ap�ndice de m�todo generado autom�ticamente

		// end-user-code
	}
}
