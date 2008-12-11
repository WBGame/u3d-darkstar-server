package ar.edu.unicen.exa.server.grid;

import java.util.ArrayList;

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
	 * Celda inicial, es decir la celda en la que aparece por defecto el
	 * jugador cuando ingresa por primera vez al mundo. 
	 */
	private Cell spawn;

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
	 * @param anStructure Estructura, siendo Cell[x=width][y=height].
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
		return spawn;
	}

	/**
	 * Retorna la coleccion de aquellas celdas que son adyacentes a la celda
	 * pasada por parametro. Se debe proporcionar tambien la posicion del
	 * jugador, para poder determinar la zona dentro de la celda en la que se
	 * encuentra, ya que puede influir en la determinacion de cuales son las
	 * celdas adyacentes. Ver documentacion del diseño sobre tratamiento de
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
		//Vector result = new Vector();
		ArrayList<Cell> result = new ArrayList<Cell>();
		int widthIterator = -1;
		int heightIterator = -1;
		for (int i = 0; i < structure.length; i++) {
			for (int j = 0; j < structure[i].length; j++) {
				if (cell.getId().equals(structure[i][j].getId())) {
					//obtengo la ubicacion de la celda actual
					widthIterator = i;
					heightIterator = j;
				}
			}
		}
		int horizontal = 0;
		int vertical = 0;
		/**
		 *  -----
		 *  |0|1|
		 *  -----
		 *  |2|3|
		 *  -----
		 */
		if (cell.getBounds().getCenterX() > position.getX()) { //left
			if (cell.getBounds().getCenterY() > position.getY()) { //top
				//posicion 0
				horizontal = -1;
				vertical = -1;
			} else {
				//posicion 2
				horizontal = -1;
				vertical = 1;
			}
		} else { //rigth
			if (cell.getBounds().getCenterY() > position.getY()) {
				//posicion 1
				horizontal = 1;
				vertical = -1;
			} else {
				//posicion 3
				horizontal = 1;
				vertical = 1;
			}
		}

		if (isInside(structure, widthIterator, heightIterator + horizontal)) {
			result.add(structure[widthIterator][heightIterator + horizontal]);
		}
		if (isInside(structure, widthIterator + vertical, heightIterator)) {
			result.add(structure[widthIterator + vertical][heightIterator]);
		}
		if (isInside(structure, 
				widthIterator + vertical, heightIterator + horizontal)) {

			result.add(
				structure[widthIterator + vertical][heightIterator + horizontal]
			);
		}
		
		Cell[] array = new Cell[result.size()];
		return result.toArray(array);
	}
	/**
	 * Este metodo permite saber si existe la celda indicada por
	 * x (ancho), y (alto) en la estructura Cell[ancho][alto].
	 * 
	 * @param matrix estructura.
	 * @param x coordenada.
	 * @param y coordenada.
	 * @return true si las coordenadas entán dentro de la estructura, false en
	 * 		otro caso.
	 */
	private static boolean isInside(final Cell[][] matrix, 
			final int x, final int y) {
		if (x < 0) {
			return false;
		}
		if (y < 0) {
			return false;
		}
		if (x >= matrix.length) {
			return false;
		}
		if (y >= matrix[x].length) {
			return false;
		}
		return true;
	}
	/**
	 * Establece la celda inicial, es decir la celda en la que aparece por
	 * defecto el jugador cuando ingresa por primera vez al mundo.
	 * 
	 * @param spawnCell celda inicial
	 * @generated "De UML a Java V5.0 
	 * 		(com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public final void setSpawnCell(final Cell spawnCell) {
		spawn = spawnCell;
	}
}
