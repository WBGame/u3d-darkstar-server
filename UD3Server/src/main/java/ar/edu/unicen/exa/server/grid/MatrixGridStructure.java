package ar.edu.unicen.exa.server.grid;

import java.awt.Rectangle;
import java.util.ArrayList;
import com.jme.math.Vector3f;
import com.sun.sgs.app.AppContext;


/** 
 *  Es una coleccion de celdas dispuestas en forma de matriz con el objetivo de
 *  particionar el espacio fisico del mundo al cual representa en zonas
 *  iguales. Posee un identificador unico que se corresponde uno a uno con el
 *  identificador del IGameState del cliente. Es decir, representa a un objeto
 *  IGameState del cliente en el servidor.
 *  
 */
public class MatrixGridStructure implements IGridStructure {
	/** The version of the serialized form of this class. */
	private static final long serialVersionUID = 3105473591463699480L;
	
	/** 
	 * Identificador unico de la estructura.
	 */
	private String worldID = null;
	
	/**
	 * Celda inicial, es decir la celda en la que aparece por defecto el
	 * jugador cuando ingresa por primera vez al mundo. 
	 */
	private Cell spawn = null;
	
	//XXX agrege la pos inicial del jugador para setearle la pos inicial 
	//cuando ingresa por primera vez y para calcular la celda spawn.
	/**
	 * Posición inicial, es decir la posicion en la que aparece por defecto el
	 * jugador cuando ingresa por primera vez al mundo. 
	 */
	private Vector3f spawnPosPlayer = null;
	
	/** 
	 *  Representacion en forma de matriz de las celdas de la estructura.
	 * @uml.annotations for <code>structure</code>
	 *     collection_type="server.grid.Cell"
	 */
	private Cell[][] structure = null;

	/**
	 * //TODO javadoc Cell[x=width][y=height].
	 * 
	 * @param gridWidth Ancho de la grilla
	 * @param gridHeight Alto de la grilla
	 * @param cellSize tamaño de la celda
	 */
	public MatrixGridStructure(final int gridWidth, final int gridHeight,
			final int cellSize) {
		//XXX parte de la inicializacion del sistema.
		//modificacion de la constructora para la inicializacion de la
		//de la matriz
		structure = new Cell[gridWidth][gridHeight];
		int height = 0;
		for (int y = 0; y < gridHeight; y++) {
			int width = 0;
			for (int x = 0; x < gridWidth; x++) {
				Rectangle bound = new Rectangle(width, height, cellSize,
						cellSize);
				structure[x][y] = new Cell(bound, this);
				width += cellSize;
			}
			height += cellSize;
		}
		//posicion del jugador inicial por defecto
		this.setSpawnPosition(50, 0, 50);
	}
	
	/**
	 * Setea el identificador único de la estructura.
	 * 
	 * @param id identificador único.
	 */
	public final void setIdWorld(final String id) {
		AppContext.getDataManager().markForUpdate(this);
		this.worldID = id;
	}

	/**
	 * Retorna el identificador único de la estructura.
	 * 
	 * @return identificador único.
	 */
	public final String getIdWorld() {
		return this.worldID;
	}

	/**
	 * Retorna la celda que contiene la posición "position".
	 * 
	 * @param position posición a evaluar.
	 * 
	 * @return la celda que contiene la posición "position". <code>null</code>
	 * si la posición "position" está fuera de la grilla.
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
	 * 
	 * @return un vector vacío ya que esta implementación contiene una sola 
	 * 		celda.

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
	 * 
	 * @return true si las coordenadas entán dentro de la estructura, false en
	 * 		otro caso.
	 */
	private boolean isInside(final Cell[][] matrix, 
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
	 * Establece la posicion inicial, es decir la posicion en la que aparece
	 * por defecto el jugador cuando ingresa por primera vez al mundo.
	 * 
	 * @param x coord eje x
	 * @param y coord eje y
	 * @param z coord eje z
	 */
	public final void setSpawnPosition(final float x, final float y, 
			final float z) {
		AppContext.getDataManager().markForUpdate(this);
		//posición inicial del jugador en la grid
		spawnPosPlayer = new Vector3f(x, y, z);
		//obtener la celda inicial del jugador en la grid
		spawn = this.getCell(this.spawnPosPlayer);
	}
	
	/**
	 * Retorna la posicion inicial, es decir la posicion en la que aparece por
	 * defecto el jugador cuando ingresa por primera vez al mundo.
	 * 
	 * @return la posicion inicial
	 */
	public final Vector3f getSpawnPosition() {
		return spawnPosPlayer;
	}

}
