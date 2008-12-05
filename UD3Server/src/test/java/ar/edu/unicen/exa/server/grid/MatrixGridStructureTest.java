/**
 * 
 */
package ar.edu.unicen.exa.server.grid;

import static org.junit.Assert.*;

import java.awt.Rectangle;

import org.junit.Test;

import com.jme.math.Vector3f;

/**
 * @author Sebastián Perruolo
 * 
 */
public class MatrixGridStructureTest {
	/** Instancia de  MatrixGridStructure. */
	private MatrixGridStructure instance = null;
	/** Instancia de  Cell. */
	private Cell theCell = null;
	
	/** Identificador del mundo que se utilizará. */
	private static final String WORLD_ID = "anIdentifier";
	
	/** limite inferior X. */
	private static final int BOUND_X = -10;
	/** limite inferior Y. */
	private static final int BOUND_Y = -10;
	/** Ancho de la celda. */
	private static final int BOUND_WIDTH = 100;
	/** Alto de la celda. */
	private static final int BOUND_HEIGHT = 100;
	
	/**
	 * Crea una única instancia de MatrixGridStructure para los propósitos
	 * del test.
	 * 
	 * @return úna instancia de MatrixGridStructure
	 */
	private MatrixGridStructure getInstance() {
		if (instance == null) {
			Cell[][] oneCell = new Cell[1][1];
	    	String cellId = "test";
	    	Rectangle cellBunds = new Rectangle(
	    			BOUND_X, BOUND_Y, BOUND_WIDTH, BOUND_HEIGHT
	    		);
	    	theCell = new Cell(cellId, cellBunds, null);
			oneCell[0][0] = theCell;
			instance = new MatrixGridStructure(oneCell);
		}
		return instance;
	}
	/**
	 * Test method for
	 * {@link ar.edu.unicen.exa.server.grid.MatrixGridStructure
	 * 		#setIdWorld(java.lang.String)}
	 * .
	 */
	@Test
	public final void testSetIdWorld() {
		getInstance().setIdWorld(WORLD_ID);
		assertEquals(instance.getIdWorld(), WORLD_ID);
	}

	/**
	 * Test method for
	 * {@link ar.edu.unicen.exa.server.grid.MatrixGridStructure
	 * 		#getIdWorld()}.
	 */
	@Test
	public final void testGetIdWorld() {
		getInstance().setIdWorld(WORLD_ID);
		assertEquals(instance.getIdWorld(), WORLD_ID);
	}

	/**
	 * Test method for
	 * {@link ar.edu.unicen.exa.server.grid.MatrixGridStructure
	 * 		#getCell(com.jme.math.Vector3f)}
	 * .
	 */
	@Test
	public final void testGetCell() {
    	Vector3f positionInSide = new Vector3f(
    			Float.parseFloat("1"),
    			Float.parseFloat("1"),
    			Float.parseFloat("1")
    		);
    	assertEquals(getInstance().getCell(positionInSide), theCell);
    	
    	Vector3f positionOutSide = new Vector3f(
    			Float.parseFloat("200"),
    			Float.parseFloat("1"),
    			Float.parseFloat("1")
    		);
    	assertNull(getInstance().getCell(positionOutSide));
	}

	/**
	 * Test method for
	 * {@link ar.edu.unicen.exa.server.grid.MatrixGridStructure
	 * 		#getAdjacents(
	 * 			ar.edu.unicen.exa.server.grid.Cell, 
	 * 			com.jme.math.Vector3f)
	 * 		}
	 * .
	 */
	@Test
	public final void testGetAdjacents() {
		//TODO implementar!
	}

}
